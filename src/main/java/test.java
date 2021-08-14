import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


public class test {

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        File myJar = new File("./bot.jar");
        URLClassLoader child = new URLClassLoader(
                new URL[] {myJar.toURI().toURL()},
                test.class.getClassLoader()
        );
        Class<?> classToLoad = Class.forName("bots.xo.RunXO", true, child);
        Method method = classToLoad.getDeclaredMethod("main");
        Object instance = classToLoad.getDeclaredConstructor().newInstance();
        String result = (String) method.invoke(instance);
        System.out.println(result);
    }
}
