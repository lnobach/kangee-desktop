![Kangee Logo](https://raw.githubusercontent.com/lnobach/kangee-desktop/master/gfx/splash.png)

Kangee is an easy-to-use personal on-demand fileserver, enabling you to exchange files with your friends. 
Your communication partner only needs a browser to download or upload files to you. 
Unlike one-click hosting services, Kangee will not store files on any remote machine, instead, 
users become their own one-click hoster. Kangee is developed for the Java Runtime Environment, 
therefore supporting many operating systems.

For Kangee, a type of Internet access is needed that supports incoming connections, 
like many ADSL/Cable providers do. However, often mobile 3G internet access providers 
do not support this feature yet.

Since Kangee is targeted to be very user-friendly, it will also help you with the connection setup.

**Comments in the code are envisioned to be improved.**

Kangee Official Website: https://getkangee.com

To get started with the code:

1. Check out **kangee-core** (https://github.com/lnobach/kangee-core) and **kangee-desktop**.

2. To satisfy dependencies, get at least the QR code generator part of the ZXing project which can be found at https://github.com/zxing/zxing, 
or which can be obtained by downloading the zip/jar distribution of Kangee 
(http://getkangee.com/getkangee?platform=zip) and extracting 
it (in the directory /lib/ inside the zip file) and place it in Roo-Core/lib/. Kangee 
has to be linked against the library, although it is not necessary to have it included 
at runtime (but recommended for QR code support).
        
3. If you want to get custom icons, place them inside /gfx/icons/48x48/mimetypes, 
e.g. the Oxygen Icon Theme shipped with Kangee.
        
Run **kangee-desktop** with the main class **de.roo.StartSwing**, with **kangee-core** in its classpath, and 
with the additional VM argument **-splash:gfx/splash.png** to display the splash screen correctly.
