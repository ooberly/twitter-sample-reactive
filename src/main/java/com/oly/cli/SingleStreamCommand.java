package com.oly.cli;

import com.oly.social.twitter.StreamUtils;
import io.airlift.command.Command;
import rx.Observable;
import twitter4j.Status;

/**
 * User: vbeniwalx
 * Date: 1/8/14
 */
@Command(name = "single", description = "Runs a single twitter sample stream and prints out tweets per second count")
public  class SingleStreamCommand extends TweetStreamCommand{

    @SuppressWarnings("unchecked")
    public void run() {
        Observable<Status> statusObservable = StreamUtils.createObservableStream(parseCredentials().get(0));
        StreamUtils.createThrouputObersvable(statusObservable).subscribe(throughputObserver);
    }
}

