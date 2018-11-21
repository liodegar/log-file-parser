package com.wallet.parser.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "web_server_request")
public class WebServerRequest implements Serializable {

	private static final long serialVersionUID = -7288113491399238891L;

	public static final DateTimeFormatter DATE_LOG_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

	//@Convert(converter = LocalDateTimeConverter.class, attributeName = "date")
	@Column(name = "log_date")
    private LocalDateTime date;

    private String ip;

	@Column(name = "request_method")
	private String requestMethod;

	@Column(name = "http_status")
	private String httpStatus;

	private String userAgent;

	private String comments;

	public WebServerRequest() {
		setComments("Unknown blocking reasons");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(String httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		WebServerRequest that = (WebServerRequest) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (date != null ? !date.equals(that.date) : that.date != null) return false;
		if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
		if (requestMethod != null ? !requestMethod.equals(that.requestMethod) : that.requestMethod != null)
			return false;
		return !(httpStatus != null ? !httpStatus.equals(that.httpStatus) : that.httpStatus != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (date != null ? date.hashCode() : 0);
		result = 31 * result + (ip != null ? ip.hashCode() : 0);
		result = 31 * result + (requestMethod != null ? requestMethod.hashCode() : 0);
		result = 31 * result + (httpStatus != null ? httpStatus.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(300).append("WebServerRequest{");
		sb.append("date=").append(date);
		sb.append(", ip='").append(ip);
		sb.append("', requestMethod='").append(requestMethod);
		sb.append("', httpStatus='").append(httpStatus);
		sb.append("', userAgent='").append(userAgent);
		sb.append("', comments='").append(comments);
		sb.append("'}");
		return sb.toString();
	}
}

