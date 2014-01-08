package com.oly.cli;

import io.airlift.command.Cli;
import io.airlift.command.Help;

import java.util.concurrent.CountDownLatch;

/**
 * User: vbeniwalx
 * Date: 1/8/14
 */
public class Main {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws InterruptedException {

        Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("tweet-stream")
                .withDescription("experiment for twitter sample streams")
                .withDefaultCommand(Help.class)
                .withCommands(Help.class, SingleStreamCommand.class, MergingStreamCommand.class);

        Cli<Runnable> commandParser = builder.build();
        final CountDownLatch latch = new CountDownLatch(1);
        commandParser.parse(args).run();
        latch.await();
    }

}
