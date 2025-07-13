/*
 * To show verbose information pass the verbose flag e.g:
 * groovy Mixers.groovy -v
 */
import groovy.json.JsonOutput
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine

def isVerbose = args.any { it in ['--verbose', '-v'] }
def realMixerPattern = ~/(?i).*plughw:\d+,0.*/

def mixerInfos = AudioSystem.getMixerInfo() as List
def realInfos = mixerInfos.findAll { it.name ==~ realMixerPattern }
def infoMaps = realInfos.collect { mixerInfo ->
    def sourceFormats = AudioSystem.getMixer(mixerInfo)
            .getSourceLineInfo()
            .findAll { it instanceof DataLine.Info }
            .collect { it.formats }
    def targetFormats = AudioSystem.getMixer(mixerInfo)
            .getTargetLineInfo()
            .findAll { it instanceof DataLine.Info }
            .collect { it.formats }
    def basicInfo = [
        name: mixerInfo.getName(),
        description: mixerInfo.getDescription(),
        vendor: mixerInfo.getVendor(),
        version: mixerInfo.getVersion()
    ]
    def fullInfo = isVerbose
            ? [*:basicInfo,
               sourceFormats: sourceFormats,
               targetFormats: targetFormats]
            : basicInfo
    return fullInfo
}

println(JsonOutput.prettyPrint(JsonOutput.toJson(infoMaps)))
