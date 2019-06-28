package org.einnovator.blueprint.entity1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.einnovator.jpa.model.EntityBase2;
import org.einnovator.social.client.model.Channel;
import org.einnovator.social.client.model.ChannelBuilder;
import org.einnovator.social.client.model.ChannelType;
import org.einnovator.util.model.Ref;
import org.einnovator.util.model.RefBuilder;
import org.einnovator.util.model.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class MyEntity extends EntityBase2<Long> {

	@Column(length=1024)
	private String name;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(length=256)
	private String img;

	private Boolean check;
	
	@Column(length=128)
	private String channelId;

	public MyEntity() {
	}
	
	//
	// Getters and Setters
	//
	
	/**
	 * Get the value of property {@code name}.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the value of property {@code name}.
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the value of property {@code status}.
	 *
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Set the value of property {@code status}.
	 *
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}


	/**
	 * Get the value of property {@code img}.
	 *
	 * @return the img
	 */
	public String getImg() {
		return img;
	}

	/**
	 * Set the value of property {@code img}.
	 *
	 * @param img the img to set
	 */
	public void setImg(String img) {
		this.img = img;
	}

	/**
	 * Get the value of property {@code check}.
	 *
	 * @return the check
	 */
	public Boolean getCheck() {
		return check;
	}

	/**
	 * Set the value of property {@code check}.
	 *
	 * @param check the check to set
	 */
	public void setCheck(Boolean check) {
		this.check = check;
	}

	/**
	 * Get the value of property {@code channelId}.
	 *
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * Set the value of property {@code channelId}.
	 *
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public ToStringCreator toString1(ToStringCreator creator) {
		return super.toString1(creator)
				.append("name", name)
				.append("status", status)
				.append("check", check)
				.append("img", img)				
				;
	}
	
	/**
	 * @return
	 */
	public Channel makeChannel(String baseUri) {
		return new ChannelBuilder()
				.uuid(channelId)
				.name(name)
				.purpose("Discussion channel about " + name)
				.img(img)
				.thumbnail(img)
				.type(ChannelType.COMMENTS)
				.ref(makeRef(baseUri))
				.build();
	}
	
	public Ref makeRef(String baseUri) {
		return new RefBuilder()
				.id(uuid)
				.type(getClass().getSimpleName())
				.name(name)
				.img(img)
				.thumbnail(img)
				.redirectUri(baseUri + "/" + this.getClass().getSimpleName().toLowerCase() + "/" + uuid)
				.pingUri(baseUri + "/api/" + this.getClass().getSimpleName().toLowerCase() + "/"  +  uuid)
				.build();
	}

	
}
