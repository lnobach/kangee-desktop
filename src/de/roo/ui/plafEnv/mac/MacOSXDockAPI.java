package de.roo.ui.plafEnv.mac;

import java.awt.PopupMenu;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.roo.ui.plafEnv.PlatformNotSupportedException;

public class MacOSXDockAPI {

	Class<?> appClass;
	Object applicationObjInstance;

	public MacOSXDockAPI() throws PlatformNotSupportedException {

		try {

			// Doing: Application app = Application.getApplication();

			appClass = Class.forName("com.apple.eawt.Application");
			Method method = appClass.getMethod("getApplication");
			applicationObjInstance = method.invoke(null);

		} catch (ClassNotFoundException e) {
			throw new PlatformNotSupportedException("The Mac OS X Dock does not seem to be supported.", e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new PlatformNotSupportedException(e);
		} catch (IllegalArgumentException e) {
			throw new PlatformNotSupportedException(e);
		} catch (IllegalAccessException e) {
			throw new PlatformNotSupportedException(e);
		} catch (InvocationTargetException e) {
			throw new PlatformNotSupportedException(e);
		}

	}
	
	/**
	 * Sets the badge to the given text. If text is null, the badge is removed.
	 * @param text
	 */
	public void setBadge(String text) {
		
		try {
			
			Method method = appClass.getMethod("setDockIconBadge", java.lang.String.class);
			method.invoke(applicationObjInstance, text);
		
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public PopupMenu getDockMenu() {
		try {
			
			Method method = appClass.getMethod("getDockMenu");
			Object result = method.invoke(applicationObjInstance);
			return (PopupMenu)result;
			
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setDockMenu(PopupMenu menu) {
		try {
			
			Method method = appClass.getMethod("setDockMenu", PopupMenu.class);
			method.invoke(applicationObjInstance, menu);
			
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void addIconClickedListener(final IMacOSXDockIconClickedListener l) {
		 try {

			Class<?> listenerSuperInterface = Class.forName("com.apple.eawt.AppEventListener");
			Class<?> listenerInterface = Class.forName("com.apple.eawt.AppReOpenedListener");
			
			InvocationHandler listenerStub = new InvocationHandler() {
				
				@Override
				public Object invoke(Object proxy, Method method, Object[] args)
						throws Throwable {
					if ("appReOpened".equals(method.getName())) {
						l.iconClicked();
					}
					
					return null;
				}
			};
			
			
			Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{listenerInterface}, listenerStub);
			
			Method method = appClass.getMethod("addAppEventListener", listenerSuperInterface);
			
			method.invoke(applicationObjInstance, proxy);
			
		 } catch (SecurityException e) {
			 
		 } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static interface IMacOSXDockIconClickedListener {
		
		public void iconClicked();
		
	}

}






