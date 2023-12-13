package com.fo4ik.kinacademy.core.compress;

import com.fo4ik.kinacademy.exceptions.AppException;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class VideoCompressor {

    @Builder.Default
    private final Path FFMPEG_PATH = Path.of("backend/ffmpeg/bin/ffmpeg.exe");
    @Builder.Default
    private final Path FFPROBE_PATH = Path.of("backend/ffmpeg/bin/ffprobe.exe");

    //Get curren directory
    @Builder.Default
    private final String currentDirectory = System.getProperty("user.dir");

    @Async("AsyncTaskExecutor")
    public Path getVideoExtension(MultipartFile video, Path folder) {
        String extension = FilenameUtils.getExtension(video.getOriginalFilename());
        switch (extension) {
            case "mp4":
                return compressMp4(video, folder);
            case "avi":
                return compressAVI(video, folder);
             /* case "webm":
                return Path.of("webm");*/
            default:
                return null;
        }
    }

    @Async("AsyncTaskExecutor")
    Path compressMp4(MultipartFile video, Path folder) {
        FFmpegJob job = null;
        try {
            //set ffmpeg and ffprobe path
            FFmpeg ffmpeg = new FFmpeg(String.valueOf(FFMPEG_PATH));
            FFprobe ffprobe = new FFprobe(String.valueOf(FFPROBE_PATH));


            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            String inputVideoPath = UUID.randomUUID() + "-" + video.getOriginalFilename();
            Path path = Path.of(folder + "/" + UUID.fromString(FilenameUtils.removeExtension(inputVideoPath)) + "-" + UUID.randomUUID() + ".mp4");
            String outputVideoPath = String.valueOf(path);

            Files.write(Path.of(inputVideoPath), video.getBytes());

            FFmpegProbeResult in = ffprobe.probe(inputVideoPath);

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(in)
                    .addOutput(outputVideoPath.toString())
                    .setAudioCodec("aac")
                    .setVideoCodec("libx265")
                    .setFormat("mp4")
                    .setVideoFrameRate(FFmpeg.FPS_30)
                    .setAudioChannels(FFmpeg.AUDIO_STEREO)
                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                    .addExtraArgs("-tile-columns", "6")
                    .done();

            job = executor.createJob(builder, new ProgressListener() {
                final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);

                @Override
                public void progress(Progress progress) {
                    if (!"N/A".equals(progress.out_time_ns)) {
                        double percentage = (progress.out_time_ns / duration_ns) * 100;
                        System.out.println("Progress: " + (int) percentage + "%");
                    } else {
                        throw new AppException("Error while compressing video", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            });

            job.run();
            if (job.getState() == FFmpegJob.State.FAILED) {
                throw new AppException("Error while compressing video", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Files.delete(Path.of(inputVideoPath));

            System.out.println("Video path: " + path);
            return Path.of(outputVideoPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    @Async("AsyncTaskExecutor")
    Path compressAVI(MultipartFile video, Path folder) {
        try {
            FFmpeg ffmpeg = new FFmpeg(Objects.requireNonNull(getClass().getResource("/ffmpeg/bin/ffmpeg.exe")).getPath());
            FFprobe ffprobe = new FFprobe(Objects.requireNonNull(getClass().getResource("/ffmpeg/bin/ffprobe.exe")).getPath());

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            String inputVideoPath = video.getOriginalFilename();
            String outputVideoPath = folder + video.getOriginalFilename() + UUID.randomUUID();

            Files.write(Path.of(inputVideoPath), video.getBytes());

            FFmpegProbeResult in = ffprobe.probe(inputVideoPath);

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(in)
                    .addOutput(outputVideoPath)
                    .setAudioCodec("libmp3lame")
                    .setVideoCodec("mpeg4")
                    .setFormat("avi")
                    .setVideoFrameRate(FFmpeg.FPS_30)
                    .setAudioChannels(FFmpeg.AUDIO_STEREO)
                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                    .done();

            Files.delete(Path.of(inputVideoPath));
            return Path.of(outputVideoPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

}
