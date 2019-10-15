package software.amazon.awssdk.awscore.presigner;

import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.http.SdkHttpRequest;

/**
 * A generic presigned request. The isBrowserCompatible method can be used to determine whether this request
 * can be executed by a web browser.
 */
public interface PresignedRequest {
    /**
     * The URL that the presigned request will execute against. The isBrowserCompatible method can be used to
     * determine whether this request will work in a browser.
     */
    URL url();

    /**
     * The exact SERVICE time that the request will expire. After this time, attempting to execute the request
     * will fail.
     * <p>
     * This may differ from the local clock, based on the skew between the local and AWS service clocks.
     */
    Instant expiration();

    /**
     * Returns true if the url returned by the url method can be executed in a browser.
     * <p>
     * This is true when the HTTP request method is GET, and hasSignedHeaders and hasSignedPayload are false.
     * <p>
     * TODO: This isn't a universally-agreed-upon-good method name. We should iterate on it before GA.
     */
    boolean isBrowserCompatible();

    /**
     * Returns true if there are signed headers in the request. Requests with signed headers must have those
     * headers sent along with the request to prevent a "signature mismatch" error from the service.
     */
    boolean hasSignedHeaders();

    /**
     * Returns the subset of headers that were signed, and MUST be included in the presigned request to prevent
     * the request from failing.
     */
    Map<String, List<String>> signedHeaders();

    /**
     * Returns true if there is a signed payload in the request. Requests with signed payloads must have those
     * payloads sent along with the request to prevent a "signature mismatch" error from the service.
     */
    boolean hasSignedPayload();

    /**
     * Returns the payload that was signed, or Optional.empty() if hasSignedPayload is false.
     */
    Optional<SdkBytes> signedPayload();

    /**
     * The entire SigV4 query-parameter signed request (minus the payload), that can be transmitted as-is to a
     * service using any HTTP client that implement the SDK's HTTP client SPI.
     * <p>
     * This request includes signed AND unsigned headers.
     */
    SdkHttpRequest httpRequest();

    interface Builder {
        /**
         * Specifies the URL that the presigned request will execute against. The isBrowserCompatible method can be used to
         * determine whether this request will work in a browser.
         */
        Builder url(URL url);

        /**
         * The exact SERVICE time that the request will expire. After this time, attempting to execute the request
         * will fail.
         * <p>
         * This may differ from the local clock, based on the skew between the local and AWS service clocks.
         */
        Builder expiration(Instant expiration);

        /**
         * Returns true if the url returned by the url method can be executed in a browser.
         * <p>
         * This is true when the HTTP request method is GET, and hasSignedHeaders and hasSignedPayload are false.
         * <p>
         * TODO: This isn't a universally-agreed-upon-good method name. We should iterate on it before GA.
         */
        Builder isBrowserCompatible(boolean isBrowserCompatible);

        /**
         * Returns true if there are signed headers in the request. Requests with signed headers must have those
         * headers sent along with the request to prevent a "signature mismatch" error from the service.
         */
        Builder hasSignedHeaders(boolean hasSignedHeaders);

        /**
         * Returns the subset of headers that were signed, and MUST be included in the presigned request to prevent
         * the request from failing.
         */
        Builder signedHeaders(Map<String, List<String>> signedHeaders);

        /**
         * Returns true if there is a signed payload in the request. Requests with signed payloads must have those
         * payloads sent along with the request to prevent a "signature mismatch" error from the service.
         */
        Builder hasSignedPayload(boolean hasSignedPayload);

        /**
         * Returns the payload that was signed, or Optional.empty() if hasSignedPayload is false.
         */
        Builder signedPayload(SdkBytes signedPayload);

        /**
         * The entire SigV4 query-parameter signed request (minus the payload), that can be transmitted as-is to a
         * service using any HTTP client that implement the SDK's HTTP client SPI.
         * <p>
         * This request includes signed AND unsigned headers.
         */
        Builder httpRequest(SdkHttpRequest httpRequest);

        PresignedRequest build();
    }
}