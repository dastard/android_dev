package com.free.base.surfer.value.objects;

import java.util.TreeSet;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import android.text.Html;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FreebaseSearchResultVO implements Comparable<FreebaseSearchResultVO>{

	private String[] alias;
	private FreebaseSearchArticleVO article;
	private String guid;
	private String id;
	private FreebaseSearchImageVO image;
	private String name;

	@JsonProperty("relevance:score")
	private String score;

	private TreeSet<FreebaseSearchTypeVO> type;

	public String[] getAlias() {
		return alias;
	}

	public void setAlias(String[] alias) {
		this.alias = alias;
	}

	public FreebaseSearchArticleVO getArticle() {
		return article;
	}

	public void setArticle(FreebaseSearchArticleVO article) {
		this.article = article;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FreebaseSearchImageVO getImage() {
		return image;
	}

	public void setImage(FreebaseSearchImageVO image) {
		this.image = image;
	}

	public String getName() {
		if (name == null) {
			return "";
		}
		return Html.fromHtml(name).toString();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public TreeSet<FreebaseSearchTypeVO> getType() {
		return type;
	}

	public void setType(TreeSet<FreebaseSearchTypeVO> type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FreebaseSearchResultVO other = (FreebaseSearchResultVO) obj;
		if (score == null) {
			if (other.score != null)
				return false;
		} else if (!score.equals(other.score))
			return false;
		return true;
	}

	@Override
	public int compareTo(FreebaseSearchResultVO another) {
		final String thisScore = this.getScore();
		final String anotherScore = another.getScore();		
		return new Double(anotherScore).compareTo(new Double(thisScore));
	}
}
