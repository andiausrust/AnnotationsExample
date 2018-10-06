package andi.com;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) {

        System.out.println("Processing ...");

        int success = 0, failed = 0, total = 0, disabled = 0;

        // Get Annotation type
        Class<AnnotationExample> obj = AnnotationExample.class;
        System.out.println("Class<AnnotationExample>: " + obj);

        //Process @CustomTypeAnnotation
        if (obj.isAnnotationPresent(CustomTypeAnnotation.class)) {

            // Get the annotation type
            Annotation annotation = obj.getAnnotation(CustomTypeAnnotation.class);
            System.out.println("Annotation annotation: " + annotation);


            // Cast generic type annotation to CustomTypeAnnotation
            CustomTypeAnnotation custom = (CustomTypeAnnotation) annotation;
            System.out.println("CustomTypeAnnotation custom: " + custom);

            System.out.printf("%nPriority : %s", custom.priority());
            System.out.printf("%nCreatedBy : %s", custom.createdBy());
            System.out.printf("%nTags: ");

            int tagLength = custom.tags().length;

            for (String tag : custom.tags()) {

                if (tagLength >1) {
                    System.out.print(tag + ", ");
                } else {
                    System.out.print(tag);
                }
                tagLength--;
            }

            System.out.printf("%nLastModifie: %s%n%n", custom.lastModified());
        }

        //###############################################################################
        // Process @CustomMethod Annotation
        for(Method method : obj.getDeclaredMethods()){

            // if method is annotated
            if (method.isAnnotationPresent(CustomMethodAnnotation.class)){
                Annotation annotation = method.getAnnotation(CustomMethodAnnotation.class);
                CustomMethodAnnotation customMethodAnnotation = (CustomMethodAnnotation) annotation;

                // if enabled is true(default)
                if(customMethodAnnotation.enabled()){
                    String result = "n/a";

                    try {
                        result = (String) method.invoke(obj.newInstance());
                        System.out.printf("%s - customMethodAnnotation '%s' - processed - result: %n",
                                ++total,
                                method.getName(),
                                result);

                        success++;
                    }catch (Throwable ex) {
                        System.out.printf("%s - customMethod '%s' - didn't process: %s%n",
                                ++total,
                                method.getName(),
                                ex.getCause());
                        failed++;
                    }
                } else {
                    System.out.printf("%f - customMethod '%s' - didn't process%n",
                    ++total,
                    method.getName());

                    disabled++;
                }
            }
        }
        System.out.printf("%nResult : Total : %d, Successful: %d, Failed %d, Disabled %d%n",
                total,
                success,
                failed,
                disabled);

    }
}
