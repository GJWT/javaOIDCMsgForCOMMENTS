package com.auth0.jwt.oicmsg;

import com.auth0.jwt.exceptions.oicmsg_exceptions.ImportException;
import com.auth0.jwt.exceptions.oicmsg_exceptions.TypeError;
import com.auth0.jwt.exceptions.oicmsg_exceptions.UnknownKeyType;
import com.auth0.jwt.exceptions.oicmsg_exceptions.UpdateFailed;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *  Contains a set of keys that have a common origin.
 The sources can be several:
 - A dictionary provided at the initialization, see keys below.
 - A list of dictionaries provided at initialization
 - A file containing one of: JWKS, DER encoded key
 - A URL pointing to a webpage from which a JWKS can be downloaded
 */
public class KeyBundle {

    final private static Logger logger = LoggerFactory.getLogger(KeyBundle.class);
    private static final Map<String, Key> K2C =
            ImmutableMap.of(RSA_KEY, new RSAKey(),
                    "EC", new ECKey(),
                    "oct", new SYMKey()
            );
    private static final Map<String, String> map =
            ImmutableMap.of("dec", "enc",
                    "enc", "enc",
                    "ver", "sig",
                    "sig", "sig"
            );
    private static final Set<String> fileTypes = new HashSet<String>(Arrays.asList("rsa", DER, JWKS));
    private java.util.List<Key> keys;
    private Map<String, List<com.auth0.jwt.oicmsg.Key>> impJwks;
    private String source;
    private long cacheTime;
    private boolean verifySSL;
    private String fileFormat;
    private String keyType;
    private List<String> keyUsage;
    private boolean remote;
    private long timeOut;
    private String eTag;
    private static final String JWKS = "jwks";
    private static final String JWK = "jwk";
    private static final String DER = "der";
    private static final String EC_KEY = "EC";
    private static final String RSA_KEY = "RSA";
    private static final String SYM_KEY = "SYMKey";
    private long lastUpdated;

    /**
     *
     * @param keys: A dictionary or a list of dictionaries
                    with the keys ["kty", "key", "alg", "use", "kid"]
     * @param source: Where the key set can be fetched from
     * @param verifySSL: Verify the SSL cert used by the server
     * @param fileFormat: For a local file either "jwk" or "der"
     * @param keyType: Iff local file and 'der' format what kind of key it is.
                        presently only 'rsa' is supported.
     * @param keyUsage: What the key loaded from file should be used for.
                        Only applicable for DER files
     * @throws ImportException
     */
    public KeyBundle(List<Key> keys, String source, long cacheTime, boolean verifySSL,
                     String fileFormat, String keyType, List<String> keyUsage) throws ImportException {
        this.keys = keys;
        this.cacheTime = cacheTime;
        this.verifySSL = verifySSL;
        this.fileFormat = fileFormat.toLowerCase();
        this.keyType = keyType;
        this.keyUsage = keyUsage;
        this.remote = false;
        this.timeOut = 0;
        this.impJwks = new HashMap<String, List<Key>>();
        this.lastUpdated = 0;
        this.eTag = "";

        if (keys != null) {
            this.source = null;
            //doKeys(this.keys); why is this here?
        } else {
            if ("file://".startsWith(source)) {
                this.source = source.substring(7);
            } else if ("http://".startsWith(source) || "https://".startsWith(source)) {
                this.source = source;
                this.remote = true;
            } else if (!Strings.isNullOrEmpty(source)) {
                this.source = null;
            } else {
                if (fileTypes.contains(fileFormat.toLowerCase())) {
                    File file = new File(source);
                    if (file.exists() && file.isFile()) {
                        this.source = source;
                    } else {
                        throw new ImportException("No such file exists");
                    }
                } else {
                    throw new ImportException("Unknown source");
                }
            }

            if (!this.remote) {
                if (this.JWKS.equals(fileFormat) || this.JWK.equals(fileFormat)) {
                    try {
                        this.doLocalJwk(this.source);
                    } catch (UpdateFailed updateFailed) {
                        logger.error("Local key updated from " + this.source + " failed.");
                    }
                } else if (this.fileFormat.equals(DER)) {
                    doLocalDer(this.source, this.keyType, this.keyUsage);
                }
            }
        }
    }

    public KeyBundle() throws ImportException {
        this(null, "", 300, true, JWK, RSA_KEY, null);
    }

    public KeyBundle(List<Key> keyList, String keyType) throws ImportException {
        this(keyList, "", 300, true, JWK, keyType, null);
    }

    public KeyBundle(List<Key> keyList, String keyType, List<String> usage) throws ImportException {
        this(keyList, "", 300, true, JWK, keyType, usage);
    }

    public KeyBundle(String source, boolean verifySSL) throws ImportException {
        this(null, source, 300, verifySSL, JWK, RSA_KEY, null);
    }

    public KeyBundle(String source, String fileFormat, List<String> usage) throws ImportException {
        this(null, source, 300, true, fileFormat, RSA_KEY, usage);
    }

    /**
     * Go from JWK description to binary keys
     * @param keys
     */
    public void doKeys(List<Key> keys) {
        for (Key keyIndex : keys) {
            final String kty = keyIndex.getKty();
            List<String> usage = harmonizeUsage(Arrays.asList(keyIndex.getUse()));
            keys.remove("use");
            boolean flag = false;
            for (String use : usage) {
                if (RSA_KEY.equalsIgnoreCase(kty)) {
                    key = new RSAKey(use);
                } else if (EC_KEY.equalsIgnoreCase(kty)) {
                    key = new ECKey(use);
                } else if (SYM_KEY.equalsIgnoreCase(kty)) {
                    key = new SYMKey(use);
                } else {
                    throw new IllegalArgumentException("Encryption type: " + typeIndex + " isn't supported");
                }
                this.keys.add(key);
                flag = true;
            }

            if (!flag) {
                logger.warn("While loading keys, UnknownKeyType: " + kty);
            }
        }
    }

    private static List<String> harmonizeUsage(List<String> uses) {
        Set<String> keys = map.keySet();
        Set<String> usagesSet = new HashSet<>();
        for (String use : uses) {
            if (keys.contains(use)) {
                usagesSet.add(use);
            }
        }
        return new ArrayList<>(usagesSet);
    }

    /**
     * Load a JWKS from a local file
     * @param fileName
     * @throws UpdateFailed
     */
    public void doLocalJwk(String fileName) throws UpdateFailed {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(
                    fileName));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray keys = (JSONArray) jsonObject.get("keys");
            Iterator<String> iterator = keys.iterator();
            List<Key> keysList = new ArrayList<Key>();
            Gson gson = new Gson();
            while (iterator.hasNext()) {
                keysList.add(gson.fromJson(iterator.next(), Key.class));
            }
            doKeys(keysList);
        } catch (Exception e) {
            logger.error("Now 'keys' keyword in JWKS");
            throw new UpdateFailed("Local key updated from " + fileName + " failed.");
        } finally {
            this.lastUpdated = System.currentTimeMillis();
        }
    }

    /**
     * Load a DER encoded file and create a key from it.
     * @param fileName
     * @param keyType: Presently only 'rsa' is supported
     * @param keyUsage: encryption ('enc') or signing ('sig') or both
     * @throws NotImplementedException
     */
    public void doLocalDer(String fileName, String keyType, List<String> keyUsage) throws NotImplementedException {
        RSAKey rsaKey = rsaLoad(fileName);

        if (!keyType.equalsIgnoreCase(RSA_KEY)) {
            throw new NotImplementedException();
        }

        if (keyUsage.isEmpty()) {
            keyUsage = new ArrayList<String>() {{
                add("enc");
                add("sig");
            }};
        } else {
            keyUsage = harmonizeUsage(keyUsage);
        }

        for (String use : keyUsage) {
            RSAKey key = new RSAKey().loadKey(rsaKey);
            key.setUse(use);
            this.keys.add(key);
        }
        this.lastUpdated = System.currentTimeMillis();
    }

    /**
     * Load a JWKS from a webpage
     * @return True or False if load was successful
     * @throws UpdateFailed
     * @throws KeyException
     */
    public boolean doRemote() throws UpdateFailed, KeyException {
        Map<String, Object> args = new HashMap<>();
        args.put("verify", this.verifySSL);
        if (!this.eTag.isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("If-None-Match", this.eTag);
            args.put("headers", jsonObject);
        }

        int statusCode;
        HttpResponse response;
        try {
            logger.debug("KeyBundle fetch keys from: " + this.source);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(this.source);
            response = httpclient.execute(httpget);
            statusCode = response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new UpdateFailed("Couldn't make GET request to url: " + this.source);
        }

        if (statusCode == 304) {
            this.timeOut = System.currentTimeMillis() + this.cacheTime;
            this.lastUpdated = System.currentTimeMillis();

            List<Key> keys = this.impJwks.get("keys");
            if (keys != null) {
                doKeys(keys);
            } else {
                logger.error("No 'keys' keyword in JWKS");
                throw new UpdateFailed("No 'keys' keyword in JWKS");
            }
        } else if (statusCode == 200) {
            this.timeOut = System.currentTimeMillis() + this.cacheTime;
            try {
                this.impJwks = parseRemoteResponse(response);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            if (!this.impJwks.keySet().contains("keys")) {
                throw new UpdateFailed(this.source);
            }

            logger.debug("Loaded JWKS: " + response.toString() + " from " + this.source);
            List<Key> keys = this.impJwks.get("keys");
            if (keys != null) {
                doKeys(keys);
            } else {
                logger.error("No 'keys' keyword in JWKS");
                throw new UpdateFailed("No 'keys' keyword in JWKS");
            }

            Header[] headers = response.getHeaders("Etag");
            if (headers != null) {
                this.eTag = headers;
            } else {
                throw new KeyException("No 'Etag' keyword in headers");
            }
        } else {
            throw new UpdateFailed("Source: " + this.source + " status code: " + statusCode);
        }

        this.lastUpdated = System.currentTimeMillis();
        return true;
    }

    /**
     *  Parse JWKS from the HTTP response.
        Should be overriden by subclasses for adding support of e.g. signed
        JWKS.
     * @param response: HTTP response from the 'jwks_uri' endpoint
     * @return response parsed as JSON
     * @throws IOException
     * @throws ParseException
     */
    private JSONObject parseRemoteResponse(HttpResponse response) throws IOException, ParseException {
        if (!response.getHeaders("Content-Type").equals("application/json")) {
            logger.warn("Wrong Content_type");
        }

        logger.debug(String.format("Loaded JWKS: %s from %s", response.toString(), this.source));

        return (JSONObject) new JSONParser().parse(EntityUtils.toString(response.getEntity()));
    }

    private boolean upToDate() {

        boolean result = false;
        if (!this.keys.isEmpty()) {
            if (this.remote && System.currentTimeMillis() > this.timeOut && update()) {
                result = true;
            }
        } else if (this.remote) {
            if (update()) {
                result = true;
            }
        }

        return result;
    }

    /**
     * Reload the keys if necessary
       This is a forced update, and it will only happen if cache time has not elapsed
       Replaced keys will be marked as inactive and not removed.
     * @return True or False
     */
    public boolean update() {
        boolean result = true;
        if (!this.source.isEmpty()) {
            List<Key> keys = this.keys;
            this.keys = new ArrayList<Key>();
            try {
                if (!this.remote) {
                    if (this.fileFormat.equals(JWKS)) {
                        this.doLocalJwk(this.source);
                    } else if (this.fileFormat.equals(DER)) {
                        doLocalDer(source, keyType, keyUsage);
                    }
                } else {
                    result = doRemote();
                }
            } catch (Exception exception) {
                logger.error("Key bundle updated failed: " + exception.toString());
                this.keys = keys;
                return false;
            }

            for (Key key : keys) {
                if (!keys.contains(key)) {
                    if (key.getInactiveSince() == Long.MIN_VALUE) {
                        key.setInactiveSince();
                    }
                }
                this.keys.add(key);
            }
        }
        return result;
    }

    /**
     * Return a list of keys. Either all keys or only keys of a specific type
     * @param typ: Type of key (rsa, ec, oct, ..)
     * @return If typ is undefined, return all the keys as a dictionary
                otherwise the appropriate keys in a list
     */
    public List<Key> get(String typ) {

        this.upToDate();
        List<String> types = Arrays.asList(typ.toLowerCase(), typ.toUpperCase());

        if (!typ.isEmpty()) {
            List<Key> keys = new ArrayList<>();
            for (Key key : this.keys) {
                if (types.contains(key.getKty())) {
                    keys.add(key);
                }
            }
            return keys;
        } else {
            return this.keys;
        }
    }

    /**
     * Return all keys after having updated them
     * @return List of all keys
     */
    public List<Key> getKeys() {
        this.upToDate();
        return this.keys;
    }

    public String getSource() {
        return source;
    }

    public List<Key> getActiveKeys() {
        List<Key> activeKeys = new ArrayList<>();
        for (Key key : this.keys) {
            if (key.getInactiveSince() == 0) {
                activeKeys.add(key);
            }
        }

        return activeKeys;
    }

    /**
     * Remove keys that are of a specific kind or kind and value.
     * @param typ: Type of key (rsa, ec, oct, ..)
     */
    public void removeKeysByType(String typ) {
        List<String> types = Arrays.asList(typ.toLowerCase(), typ.toUpperCase());

        for (Key key : this.keys) {
            if (!types.contains(key.getKty())) {
                this.keys.remove(key);
            }
        }
    }

    @Override
    public String toString() {
        return this.jwks();
    }

    /**
     * Create a JWKS
     * @param isPrivate: Whether private key information should be included.
     * @return: A JWKS representation of the keys in this bundle
     */
    public String jwks(boolean isPrivate) {
        this.upToDate();
        List<Key> keys = new ArrayList<>();
        Key key;
        for (Key keyIndex : this.keys) {
            if (isPrivate) {
                key = keyIndex.serialize(isPrivate);
            } else {
                key = keyIndex.toDict();
                //TODO
            }
        }
    }

    public String jwks() {
        return jwks(false);
    }

    /**
     * Add a key to the list of keys in this bundle
     * @param key: Key to be added
     */
    public void append(Key key) {
        this.keys.add(key);
    }

    /**
     * Remove a specific key from this bundle
     * @param key: The key that should be removed
     */
    public void remove(Key key) {
        this.keys.remove(key);
    }

    /**
     * The number of keys
     * @return: The number of keys
     */
    public int getLength() {
        return this.keys.size();
    }

    /**
     * Return the key that is specified by key ID (kid)
     * @param kid: The Key ID
     * @return The key or None
     */
    public Key getKeyWithKid(String kid) {
        for (Key key : this.keys) {
            if (key.getKid().equals(kid)) {
                return key;
            }
        }

        //Try updating since there might have been an update to the key file
        update();

        for (Key key : this.keys) {
            if (key.getKid().equals(kid)) {
                return key;
            }
        }

        return null;
    }

    /**
     *  Return a list of key IDs. Note that list may be shorter than
        the list of keys.
     * @return A list of all the key IDs that exists in this bundle
     */
    public List<String> getKids() {
        this.upToDate();
        List<String> kids = new ArrayList<>();
        for (Key key : this.keys) {
            if (!key.getKid().isEmpty()) {
                kids.add(key.getKid());
            }
        }

        return kids;
    }

    /**
     * Mark a specific key as inactive based on the key's KeyID
     * @param kid: The Key Identifier
     */
    public void markAsInactive(String kid) {
        Key key = getKeyWithKid(kid);
        key.setInactiveSince();
    }

    /**
     *  Remove keys that should not be available anymore.
        Outdated means that the key was marked as inactive at a time
        that was longer ago than what is given in 'after'.
     * @param after: The length of time the key will remain in the KeyBundle
                      before it should be removed.
     * @param when: To make it easier to test
     * @throws TypeError
     */
    public void removeOutdated(float after, int when) throws TypeError {
        long now;
        if (when != 0) {
            now = when;
        } else {
            now = System.currentTimeMillis();
        }

        List<Key> keys = new ArrayList<>();
        for (Key key : this.keys) {
            if (!(key.getInactiveSince() && (key.getInactiveSince() + after < now))) {
                keys.add(key);
            }
        }

        this.keys = keys;
    }


    /**
     * Create a KeyBundle based on the content in a local file
     * @param filename: Name of the file
     * @param type: Type of content
     * @param usage: What the key should be used for
     * @return The created KeyBundle
     * @throws ImportException
     * @throws UnknownKeyType
     */
    public KeyBundle keyBundleFromLocalFile(String filename, String type, List<String> usage) throws ImportException, UnknownKeyType {
        usage = harmonizeUsage(usage);
        KeyBundle keyBundle;
        type = type.toLowerCase();
        if (JWKS.equals(type)) {
            keyBundle = new KeyBundle(filename, JWKS, usage);
        } else if (DER.equals(type)) {
            keyBundle = new KeyBundle(filename, DER, usage);
        } else {
            throw new UnknownKeyType("Unsupported key type");
        }

        return keyBundle;
    }

    /**
     * Write a JWK to a file. Will ignore symmetric keys !!
     * @param kbl: List of KeyBundles
     * @param target: Name of the file to which everything should be written
     * @param isPrivate: Should the private parts also be exported
     */
    public void dumpJwks(List<KeyBundle> kbl, String target, boolean isPrivate) {
        throw new UnsupportedOperationException();
    }


}
