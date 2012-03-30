package com.free.base.surfer.value.objects;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TreeMap;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FreebaseTopicVO implements Comparable<FreebaseTopicVO> {

	private Bitmap thumbnailImage;
	private String[] alias;
	private String description;
	private String id;
	private String text;
	private String thumbnail;
	private String url;
	private FreebaseTopicTypeVO[] type;
	private FreebaseWebpageVO[] webpage;
	private TreeMap<String, TopicPropertyContainer> topicProperties;

	public String getDescription() {
		if (TextUtils.isEmpty(description)) {
			return "";
		}
		return Html.fromHtml(description).toString();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Spanned getUrl() {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		return Html.fromHtml("<a href=\"" + url.trim() + "\">Freebase link</a>");
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public FreebaseTopicTypeVO[] getType() {
		return type;
	}

	public void setType(FreebaseTopicTypeVO[] type) {
		this.type = type;
	}

	public String[] getAlias() {
		return alias;
	}

	public void setAlias(String[] alias) {
		this.alias = alias;
	}

	public TreeMap<String, TopicPropertyContainer> getTopicProperties() {
		return topicProperties;
	}

	public void setTopicProperties(TreeMap<String, TopicPropertyContainer> topicProperties) {
		this.topicProperties = topicProperties;
	}

	public Bitmap getBitmapFromURL() {
		try {
			final URL url = new URL(getThumbnail());
			final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			final InputStream input = connection.getInputStream();
			final Bitmap myBitmap = BitmapFactory.decodeStream(input);
			setThumbnailImage(myBitmap);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Bitmap getThumbnailImage() {
		return thumbnailImage;
	}

	public void setThumbnailImage(final Bitmap thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}

	public FreebaseWebpageVO[] getWebpage() {
		return webpage;
	}

	public void setWebpage(FreebaseWebpageVO[] webpage) {
		this.webpage = webpage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FreebaseTopicVO other = (FreebaseTopicVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(FreebaseTopicVO another) {
		return this.getId().compareTo(another.getId());
	}

}
