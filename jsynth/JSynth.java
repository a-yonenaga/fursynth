//jsynth.java
//written by a.yonenaga
//

/**
 *
 *
 *
 */
package jsynth;

import javax.sound.sampled.*;

public class JSynth{
	static int C=0,CS=1,D=2,DS=3,E=4,F=5,FS=6,G=7,GS=8,A=9,AS=10,B=11;
	private int BPM = 120;
	private int SAMPLE_RATE = 44100;
	//���g�����v�Z���ăe�[�u���ɃZ�b�g����
	static double note_freq[] = new double [128];
	  
	static int getNoteNo(int octave, int noteName){
		return octave * 12 + noteName;//��������NoteNo���v�Z����
	}
	static void calcFrequency(){
		//����(2��12�捪)���v�Z
		double r = Math.pow(2.0, 1.0 /12.0);
		// A(NoteNo=69)����̉����v�Z
		note_freq[69] = 440.0; //A�̃m�[�g
		for ( int i = 70; i < 128; i++){
			note_freq[i] = note_freq[i-1] * r;
		}
		//A��艺�̉����v�Z
		for ( int i = 70; i >= 0; i--){
			note_freq[i] = note_freq[i+1] / r;
		}
		//�ꗗ��\��
		for ( int i = 0; i < 128; i++){
			System.out.println(i + "," + note_freq[i]);
		}
		return;
	}
	static double getNoteNoToFreq(int octave, int noteName) {
		return note_freq[getNoteNo(octave, noteName)];
	}
	/**
	 * n�������̒������v�Z����
	 * @param n������
	 */
	int getSampleCount(int nLength){
		double n4 = (60.0 / this.BPM) * SAMPLE_RATE; //�l�������̃T���v����
		double n1 = n4 * 4; //�S�����̃T���v����
		return (int)Math.round(n1 / nLength);
	}

	public void showName(){
		System.out.println("package JSynth");
	}

	public static void main(String[] args){
		JSynth js = new JSynth();
		PlaySquareWave myosc = new PlaySquareWave();
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

