import groovy.json.JsonOutput
import java.util.stream.Collectors
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine

def mixerInfos = AudioSystem.getMixerInfo() as List
def infoMaps = mixerInfos.stream().map { mixerInfo ->
    def sourceFormats = AudioSystem.getMixer(mixerInfo)
            .getSourceLineInfo()
            .stream()
            .filter { it instanceof DataLine.Info }
            .map { it.formats }
            .collect(Collectors.toList())
    def targetFormats = AudioSystem.getMixer(mixerInfo)
            .getTargetLineInfo()
            .stream()
            .filter { it instanceof DataLine.Info }
            .map { it.formats }
            .collect(Collectors.toList())
    def fullInfo = [
        name: mixerInfo.getName(),
        description: mixerInfo.getDescription(),
        vendor: mixerInfo.getVendor(),
        version: mixerInfo.getVersion(),
        sourceFormats: sourceFormats,
        targetFormats: targetFormats
    ]
    return fullInfo
}.collect(Collectors.toList())

println(JsonOutput.prettyPrint(JsonOutput.toJson(infoMaps)))
