package de.roo.ui.plafEnv.mac;

import de.roo.model.uiview.IAbstractLoad;
import de.roo.model.uiview.IAbstractLoad.IUploadListener;
import de.roo.model.uiview.IRooModel;
import de.roo.model.uiview.IRooModelListener;
import de.roo.model.uiview.IRooResource;
import de.roo.model.uiview.IRooResource.IResourceListener;

public abstract class AllLoadsListener {
	
	public AllLoadsListener(IRooModel mdl) {
		
		for (int i = 0; i < mdl.getNumResources(); i++) {
			IRooResource res = mdl.getResourceAt(i);
			res.addResourceListener(new ResourceListenerImpl());
		}
		mdl.addListener(new RooModelListenerImpl());
		
	}
	
	class RooModelListenerImpl implements IRooModelListener {
	
		@Override
		public void resourceChanged(IRooModel mdl, IRooResource res, int index) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void resourceAdded(IRooModel mdl, IRooResource res, int index) {
			res.addResourceListener(new ResourceListenerImpl());
		}
	
		@Override
		public void resourceRemoved(IRooModel mdl, IRooResource res, int index) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class ResourceListenerImpl implements IResourceListener {

		@Override
		public void resourceChanged(IRooResource resource) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void resourceRemoved(IRooResource resource) {
			//resource.removeResourceListener(this); throws a Concurrent Modification Exception
		}

		@Override
		public void loadAdded(IRooResource res, IAbstractLoad upl, int idx) {
			upl.addListener(new UploadListenerImpl());
		}

		@Override
		public void loadRemoved(IRooResource res, IAbstractLoad upl, int idx) {
			//Nothing to do
		}
		
	}
	
	class UploadListenerImpl implements IUploadListener {

		@Override
		public void stateChanged(IAbstractLoad upload) {
			onLoadStateChange(upload);
		}
		
	}

	public abstract void onLoadAdded(IRooResource res, IAbstractLoad upl, int idx);

	public abstract void onLoadRemoved(IRooResource res, IAbstractLoad upl, int idx);
	
	public abstract void onLoadStateChange(IAbstractLoad l);
	
}
