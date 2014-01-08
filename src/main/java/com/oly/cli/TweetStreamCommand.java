package com.oly.cli;

import com.oly.social.twitter.TwitterCredential;
import io.airlift.command.Option;
import io.airlift.command.OptionType;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import rx.Observer;

import java.util.List;

/**
 * User: vbeniwalx
 * Date: 1/8/14
 */
public abstract class TweetStreamCommand implements Runnable {
    @Option(type = OptionType.GLOBAL, name = "-c", description = "twitter credentials yaml file")
    public String twitterCredentialsFile;

    public Observer<Integer> throughputObserver = new Observer<Integer>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(Integer throughput) {
            System.out.println("TPS:"+throughput);
        }
    };

    List<TwitterCredential> parseCredentials() {
        Constructor constructor = new Constructor();
        constructor.addTypeDescription(new TypeDescription(TwitterCredential.class, "!credential"));
        Yaml yaml = new Yaml(constructor);
        return (List<TwitterCredential>) yaml.load(getClass().getResourceAsStream("/" + twitterCredentialsFile));
    }


}
