package org.eclipse.tum.servicecomputing.searchEngine;

import java.util.List;

public interface WSSearchEngine {

	public List<ServiceInfo> search(String keywords);
}
