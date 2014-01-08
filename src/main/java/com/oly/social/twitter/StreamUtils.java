package com.oly.social.twitter;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import rx.util.functions.Action0;
import rx.util.functions.Func1;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * User: vbeniwalx
 * Date: 1/8/14
 */
public class StreamUtils {


    public static Observable<Integer> createThroughputObservable(Observable<Status> statusObservable){
         return statusObservable.buffer(1, TimeUnit.SECONDS).map(new Func1<List<Status>, Integer>() {
            @Override
            public Integer call(List<Status> statuses) {
                return statuses.size();
            }
        });
    }

    public static Observable<Status> createObservableStream(final TwitterCredential twitterCredential) {
        return Observable.create(new Observable.OnSubscribeFunc<Status>() {
            @Override
            public Subscription onSubscribe(final Observer<? super Status> statusObserver) {
                final TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
                twitterStream.setOAuthConsumer(twitterCredential.getConsumerKey(), twitterCredential.getConsumerSecret());
                twitterStream.setOAuthAccessToken(new AccessToken(twitterCredential.getAccessToken(), twitterCredential.getAccessTokenSecret()));
                twitterStream.addListener(new StatusAdapter() {

                    public void onStatus(Status status) {
                        statusObserver.onNext(status);
                    }

                    public void onException(Exception ex) {
                        statusObserver.onError(ex);
                    }
                });
                twitterStream.sample();
                return Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        twitterStream.cleanUp();
                    }
                });
            }
        });

    }

}
