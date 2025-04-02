package io.getunleash.example;

import io.getunleash.DefaultUnleash;
import io.getunleash.Unleash;
import io.getunleash.UnleashContext;
import io.getunleash.UnleashException;
import io.getunleash.event.IsEnabledImpressionEvent;
import io.getunleash.event.ToggleEvaluated;
import io.getunleash.event.UnleashEvent;
import io.getunleash.event.UnleashSubscriber;
import io.getunleash.util.UnleashConfig;

public class AdvancedConstraints {

    public static void main(String[] args) throws InterruptedException {
        try {
            UnleashConfig config = UnleashConfig.builder()
                    .appName("client-example.advanced.java")
                    .customHttpHeader(
                            "Authorization",
                            getOrElse("UNLEASH_API_TOKEN",
                                    "*:development.25a06b75248528f8ca93ce179dcdd141aedfb632231e0d21fd8ff349"))
                    .unleashAPI(getOrElse("UNLEASH_API_URL", "https://app.unleash-hosted.com/demo/api"))
                    .instanceId("java-example")
                    .synchronousFetchOnInitialisation(true)
                    .fetchTogglesInterval(5)
                    .sendMetricsInterval(60)
                    .subscriber(new UnleashSubscriber() {
                        @Override
                        public void on(UnleashEvent unleashEvent) {
                            if (!(unleashEvent instanceof ToggleEvaluated) && !(unleashEvent instanceof IsEnabledImpressionEvent)) {
                                System.out.println(unleashEvent.toString());
                            }
                        }
                        @Override
                        public void onError(UnleashException unleashException) {
                            System.out.println("\t\t == "+unleashException.getMessage());
                        }
                    })
                    .sendMetricsInterval(1)
                    .build();

            
            

            final Unleash unleash = new DefaultUnleash(config);
            int tn = 5;
            Thread[] threads = new Thread[tn];
            for (int i = 0; i < tn; i++) {
                threads[i] = new Thread(() -> {
                    int contextEnabled = 0;
                    int smallerSemverEnabled = 0;
                    for (int j = 0; j < 1000; j++) {
                        final UnleashContext context = UnleashContext.builder()
                            .addProperty("appVersion", "1.5.2")
                            .addProperty("thread", ""+Thread.currentThread().threadId())
                            .addProperty("userId", ""+Math.random())
                            .build();
                        final UnleashContext smallerSemver = UnleashContext.builder()
                            .addProperty("appVersion", "1.1.0")
                            .addProperty("thread", ""+Thread.currentThread().threadId())
                            .build();
                        if (unleash.isEnabled("advanced.constraints", context)) {
                            contextEnabled++;
                        }
                        if (unleash.isEnabled("advanced.constraints", smallerSemver)) {
                            smallerSemverEnabled++;
                        }
                        try {
                            long random = 25L + (long) (Math.random());
                            Thread.sleep(random);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("Thread interrupted: " + Thread.currentThread().getName());
                            return;
                        }
                    }
                    System.out.println("Thread " + Thread.currentThread().getName() +
                            " - Context enabled: " + contextEnabled);
                    System.out.println("Thread " + Thread.currentThread().getName() +
                            " - Smaller semver enabled: " + smallerSemverEnabled);
                });
                threads[i].start();
            }

            // Wait for all threads to finish
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getOrElse(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null) ? defaultValue : value;
    }
}
