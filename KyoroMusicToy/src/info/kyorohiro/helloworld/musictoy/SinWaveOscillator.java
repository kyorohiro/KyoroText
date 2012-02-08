package info.kyorohiro.helloworld.musictoy;

public class SinWaveOscillator {

	public static final int FREQUENCY = 44100;
	public void generateSineWave(double frequencyOfOscillation, double amplitudeOfTheSignal,
			short[] data, int position, int length) {
		int start = position;
		int end = position+length;
		double fc = frequencyOfOscillation;
		double ac = amplitudeOfTheSignal;

		int a=0;
		for(int time=start;time<end;time++)
		{
			double v = 0;
			v = ac*amplitudeOfTheSignal*Math.sin(time*2*Math.PI*(fc/FREQUENCY));
			data[a++]= (short)v;
		}
	}

	public static double toHz(int note) {
		return 440*Math.pow(2, (note-69)/12);
	}
}

/*
public static function generateFMWave(
		data:ByteArray, position:Number,
		amplitudeOfTheSignal:Number, frequencyOfOscillation:Number, amptitudeOfModular:Number,length:Number=2048):void{

	var start:Number = position;
var end:Number = position+length;
var ac:Number = amplitudeOfTheSignal;
var fc:Number = frequencyOfOscillation;
var am:Number = amptitudeOfModular;
var signalTemp:Number = 1;
var signalR:Number = 1;
var signalL:Number = 1;

var currentPosition:Number = data.position;

for(var time:int=start;time<end;time++)
{
	if(data.position+32<data.length){
		signalR = data.readFloat();
		signalL = data.readFloat();
	} else {
		signalR = 0;
		signalL = 0;					
	}
	signalR = ac*Math.sin(2*Math.PI*(fc/AS3_DYNAMIC_SOUND_FREQUENCY)*time + am*signalR);
	signalL = ac*Math.sin(2*Math.PI*(fc/AS3_DYNAMIC_SOUND_FREQUENCY)*time + am*signalL);
	data.writeFloat(signalR);
	data.writeFloat(signalL);
}
}


*/
