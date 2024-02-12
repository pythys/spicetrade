import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import groovy.json.JsonOutput

def mixerInfos = AudioSystem.getMixerInfo() as List
mixerInfos.each { mixerInfo ->
    def mixerMap = [
        name: mixerInfo.getName(),
        vendor: mixerInfo.getVendor(),
        version: mixerInfo.getVersion(),
        description: mixerInfo.getDescription()
    ]
    def mixer = AudioSystem.getMixer(mixerInfo)
    def sourceLineInfo = mixer.getSourceLineInfo()
    sourceLineInfo.each { info ->
        if (info instanceof DataLine.Info) {
            def dataLineInfo = (DataLine.Info) info
            def supportedFormats = dataLineInfo.formats
            supportedFormats.each { format ->
                def formatMap = [
                    *:mixerMap,
                    sampleRate: format.sampleRate,
                    sampleSizeInBits: format.sampleSizeInBits,
                    channels: format.channels,
                    frameSize: format.frameSize,
                    frameRate: format.frameRate,
                    bigEndian: format.bigEndian
                ]
            println(JsonOutput.prettyPrint(JsonOutput.toJson(formatMap)))
            }
        }
    }
}
