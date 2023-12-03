package com.fo4ik.kinacademy.core.compress;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class VideoCompressor {
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public Future<Path> compressVideoAsync(MultipartFile video) {
        return executorService.submit(() -> compressVideo(video));
    }

    public void shutdown() {
        executorService.shutdown();
    }


    private Path compressVideo(MultipartFile video) {
        try {
            FFmpeg ffmpeg = new FFmpeg(Objects.requireNonNull(getClass().getResource("/ffmpeg/bin/ffmpeg.exe")).getPath());
            FFprobe ffprobe = new FFprobe(Objects.requireNonNull(getClass().getResource("/ffmpeg/bin/ffprobe.exe")).getPath());

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            String inputVideoPath = video.getOriginalFilename();
            String outputVideoPath = "compressed_" + video.getOriginalFilename() + UUID.randomUUID();

            Files.write(Path.of(inputVideoPath), video.getBytes());

            FFmpegProbeResult in = ffprobe.probe(inputVideoPath);

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(in)
                    .addOutput(outputVideoPath)
                    .setAudioCodec("aac")
                    .setVideoCodec("libx265")
                    .setFormat("mp4")
                    .setVideoFrameRate(FFmpeg.FPS_24)
                    .setAudioChannels(FFmpeg.AUDIO_STEREO)
                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                    .addExtraArgs("-tile-columns", "6")
                    .done();

           /* FFmpegJob job = executor.createJob(builder, new ProgressListener() {
                final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);

                @Override
                public void progress(Progress progress) {
                    int percentage = (int) (progress.out_time_ns / duration_ns * 100);
                    try {
                        videoUploadHandler.sendProgress(percentage, uploadId);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            job.run();
            */
            Files.delete(Path.of(inputVideoPath));
            return Path.of(outputVideoPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
