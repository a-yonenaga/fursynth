//jsynth.java
//written by a.yonenaga
//

/**
 * FMな音のするシンセになる予定。
 * 複数のオシレータをもつ。
 *
 */
package fursynth;

import javax.sound.sampled.*;

public class FurSynth{
	static int C=0,CS=1,D=2,DS=3,E=4,F=5,FS=6,G=7,GS=8,A=9,AS=10,B=11;
	private int BPM = 120;
	private int sampleRate = 44100;
	static double note_freq[] = new double [128];

	/**
	 * 周波数テーブルを生成
	 * 
	 */
	private static void calcFrequency(){
		//半音(2の12乗根)を計算
		double r = Math.pow(2.0, 1.0 /12.0);
		// A(NoteNo=69)より上の音を計算
		note_freq[69] = 440.0; //Aのノート
		for ( int i = 70; i < 128; i++){
			note_freq[i] = note_freq[i-1] * r;
		}
		//Aより下の音を計算
		for ( int i = 70; i >= 0; i--){
			note_freq[i] = note_freq[i+1] / r;
		}
		//一覧を表示
		for ( int i = 0; i < 128; i++){
			System.out.println(i + "," + note_freq[i]);
		}
		return;
	}


	 /**
	  * 音名からNoteNoを計算する
	  */
	static int getNoteNo(int octave, int noteName){
		return octave * 12 + noteName;
	}

	/**
	 * ノート番号から周波数を引く
	 */
	static double getNoteNoToFreq(int octave, int noteName) {
		return note_freq[getNoteNo(octave, noteName)];
	}

	/**
	 * n分音符の長さを計算する
	 * @param n分音符
	 */
	int getSampleCount(int nLength){
		double n4 = (60.0 / this.BPM) * sampleRate; //四分音符のサンプル数
		double n1 = n4 * 4; //全音符のサンプル数
		return (int)Math.round(n1 / nLength);
	}

	/**
	 * main関数
	 */
	public static void main(String[] args){
		FurSynth fs = new FurSynth();
		Oscilator myosc = new Oscilator(js.sampleRate);
		calcFrequency();

		int L2 = js.getSampleCount(2);
		byte[] block = new byte[L2];


		System.out.println("Playing C-D-E");
		myosc.writeNote(523.25);//C
		myosc.writeNote(587.33);//D
		myosc.writeNote(659.26);//E	

		System.out.println("Writing some noise");
		myosc.writeNoise(block,30);
		myosc.myWrite(block, 0, block.length);
		myosc.writeNoise(block,50);
		myosc.myWrite(block, 0, block.length);

		System.out.println("writing triad");
		myosc.writeWave(block, getNoteNoToFreq(6,C), 50);
		myosc.writeWave(block, getNoteNoToFreq(6,E), 50);
		myosc.writeWave(block, getNoteNoToFreq(6,G), 50);
		myosc.drain();
	}
}

