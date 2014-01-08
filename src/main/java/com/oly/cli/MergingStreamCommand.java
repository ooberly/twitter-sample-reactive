package com.oly.cli;

import com.oly.social.twitter.StreamUtils;
import com.oly.social.twitter.TwitterCredential;
import io.airlift.command.Command;
import io.airlift.command.Option;
import rx.Observable;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * User: vbeniwalx
 * Date: 1/8/14
 */
@Command(name = "merge", description = "Merges multiple twitter sample streams into a single stream and prints out tweets per second count.")
public class MergingStreamCommand extends TweetStreamCommand {
    @Option(name = "-dropDuplicates", description = "removes the duplicate entries form the merged stream.")
    public boolean dropDuplicates;


    public void run() {
        List<Observable<Status>> observableList = new ArrayList<Observable<Status>>();
        for (TwitterCredential twitterCredential : parseCredentials()) {
            observableList.add(StreamUtils.createObservableStream(twitterCredential));
        }

        Observable<Status> mergedObservable = Observable.merge(Observable.from(observableList));

        if (dropDuplicates)
            mergedObservable = mergedObservable.distinct();

        StreamUtils.createThrouputObersvable(mergedObservable).subscribe(throughputObserver);
    }
}
