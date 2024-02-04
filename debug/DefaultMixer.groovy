import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.Line
import javax.sound.sampled.Mixer
import javax.sound.sampled.SourceDataLine
import javax.sound.sampled.TargetDataLine

AudioFormat format = new AudioFormat(44100.0f, 16, 2, true, false)
DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);
DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
Line sourceLine = AudioSystem.getLine(sourceInfo)
Line targetLine = AudioSystem.getLine(targetInfo)
Mixer sourceMixer = AudioSystem.getMixer(sourceLine.getMixer().getMixerInfo())
Mixer targetMixer = AudioSystem.getMixer(targetLine.getMixer().getMixerInfo())
println '----------------------------'
println "Default Source Mixer: ${sourceMixer.getMixerInfo().getName()}"
println "Default Target Mixer: ${sourceMixer.getMixerInfo().getName()}"
