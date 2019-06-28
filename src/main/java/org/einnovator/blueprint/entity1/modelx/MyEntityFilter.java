package org.einnovator.blueprint.entity1.modelx;

import org.einnovator.blueprint.entity1.model.Status;
import org.einnovator.util.model.ToStringCreator;

public class MyEntityFilter extends MyEntityOptions {
	
	private String q;
	
	private Status status;

	private Boolean villain;

	public MyEntityFilter() {
	}
	
	/**
	 * Get the value of property {@code q}.
	 *
	 * @return the q
	 */
	public String getQ() {
		return q;
	}

	/**
	 * Set the value of property {@code q}.
	 *
	 * @param q the q to set
	 */
	public void setQ(String q) {
		this.q = q;
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
	 * Get the value of property {@code villain}.
	 *
	 * @return the villain
	 */
	public Boolean getCheck() {
		return villain;
	}

	/**
	 * Set the value of property {@code villain}.
	 *
	 * @param villain the villain to set
	 */
	public void setCheck(Boolean villain) {
		this.villain = villain;
	}

	@Override
	public ToStringCreator toString(ToStringCreator creator) {
		return creator
			.append("q", q)
			.append("status", status)
			.append("villain", villain)
			;
	}
	
}
