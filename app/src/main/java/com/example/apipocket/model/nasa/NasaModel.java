package com.example.apipocket.model.nasa;

import com.google.gson.annotations.SerializedName;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NasaModel {

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("date")
    private String date;

    @SerializedName("url")
    private String url;

    @SerializedName("hdurl")
    private String hdurl;

    @SerializedName("featured_media_src_url")
    private String featuredMediaSrcUrl;

    @SerializedName("featured_media_url")
    private String featuredMediaUrl;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("source_url")
    private String sourceUrl;

    @SerializedName("explanation")
    private String explanation;

    @SerializedName("basic_html")
    private String basicHtml;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        String raw = null;
        // 1. En apod-basic de la NASA, hdurl contiene el enlace directo a la imagen (.jpg/.jpeg)
        if (isDirectImageUrl(hdurl)) raw = hdurl;
        else if (isDirectImageUrl(featuredMediaSrcUrl)) raw = featuredMediaSrcUrl;
        else if (isDirectImageUrl(featuredMediaUrl)) raw = featuredMediaUrl;
        else if (isDirectImageUrl(imageUrl)) raw = imageUrl;
        else if (isDirectImageUrl(sourceUrl)) raw = sourceUrl;
        else if (isDirectImageUrl(url)) raw = url;

        // 2. Si no se detectó directo por extensión, extraer de <img src="..."> en HTML
        if (raw == null) {
            String[] htmlSources = {basicHtml, content, explanation};
            for (String html : htmlSources) {
                if (html != null && !html.isEmpty()) {
                    String extracted = extractImgSrcFromHtml(html);
                    if (extracted != null) {
                        raw = extracted;
                        break;
                    }
                }
            }
        }

        // Fallback a hdurl o url si no se detectó por filtro estricto
        if (raw == null) {
            if (isHttpUrl(hdurl)) raw = hdurl;
            else if (isHttpUrl(url)) raw = url;
        }

        return sanitizeUrl(raw);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHdurl() {
        return hdurl;
    }

    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getBasicHtml() {
        return basicHtml;
    }

    private boolean isDirectImageUrl(String str) {
        if (!isHttpUrl(str)) return false;
        String lower = str.trim().toLowerCase();
        return lower.contains(".jpg") || lower.contains(".jpeg") || lower.contains(".png") || lower.contains(".webp") || lower.contains("/renditions/");
    }

    private boolean isHttpUrl(String str) {
        if (str == null || str.trim().isEmpty()) return false;
        String lower = str.trim().toLowerCase();
        return lower.startsWith("http://") || lower.startsWith("https://") || lower.startsWith("//");
    }

    private String sanitizeUrl(String urlString) {
        if (urlString == null) return null;
        String clean = urlString.trim().replace("&amp;", "&").replace("\\/", "/");
        if (clean.startsWith("//")) {
            clean = "https:" + clean;
        } else if (clean.startsWith("http://")) {
            clean = clean.replace("http://", "https://");
        }
        return clean.isEmpty() ? null : clean;
    }

    private String extractImgSrcFromHtml(String html) {
        if (html == null) return null;
        Pattern pattern = Pattern.compile("<img[^>]+src=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
