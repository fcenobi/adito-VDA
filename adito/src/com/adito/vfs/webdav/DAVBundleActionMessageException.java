package com.adito.vfs.webdav;

import com.adito.core.BundleActionMessage;

public class DAVBundleActionMessageException extends Exception {

	private static final long serialVersionUID = 2101662373784642160L;

	private BundleActionMessage bundleActionMessage;

	public DAVBundleActionMessageException(BundleActionMessage bundleActionMessage) {
		this.bundleActionMessage = bundleActionMessage;
	}
	
	public BundleActionMessage getBundleActionMessage(){
		return bundleActionMessage;
	}
	
}
