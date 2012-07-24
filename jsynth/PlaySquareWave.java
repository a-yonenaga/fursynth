//PlaySquareWave.java
package jsynth;

import javax.sound.sampled.*;

public class PlaySquareWave{
	static int SAMPLE_RATE;
	SourceDataLine line;

	public PlaySquareWave(){
		SAMPLE_RATE = 44100; //44.1KHz
		//�I�[�f�B�I�`�����w��
		AudioFormat af = new AudioFormat(
			SAMPLE_RATE, 8, 1, true, true);
		try{
			DataLine.Info info = new DataLine.Info(
				SourceDataLine.class, af);
			line = (SourceDataLine)AudioSystem.getLine(info);
			line.open();
			line.start();
		}catch(LineUnavailableException e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		PlaySquareWave myosc = new PlaySquareWave();
		System.out.println("Playing C-D-E");
		myosc.writeNote(523.25);//C
		myosc.writeNote(587.33);//D
		myosc.writeNote(659.26);//E
	}

	public void writeNote(double frequency) {
		byte[] b = new byte[SAMPLE_RATE];
		for(int i = 0; i < b.length; i++){
			double r = i / (SAMPLE_RATE / frequency);
			b[i] = (byte)((Math.round(r) % 2 == 0) ? 100 : -100);
		}
		//�Đ�(�o�C�g���line�ɏ�������)
		line.write(b, 0, b.length);
		line.drain();//�I���܂�
	}
	/**
	 * �Đ��I���܂őҋ@����drain�̃��b�p
	 *
	 */
	public void drain(){
		line.drain();
	}
	/**
	 * �o�C�g����o�͂��郉�b�p
	 */
	public void myWrite(byte[] b, int n, int length){
		line.write(b, n, length);
	}
	public void writeNote(double frequency, int sampleCount) {
		byte[] b = new byte[SAMPLE_RATE];
		double amplitude = SAMPLE_RATE / frequency; //�g��
		for(int i = 0; i < b.length; i++){
			double r = i / amplitude;
			b[i] = (byte)((Math.round(r) % 2 == 0) ? 100 : -100);
		}
		//�Đ�(�o�C�g���line�ɏ�������)
		line.write(b, 0, b.length);
	}
	public void writeWave(byte[] b, double frequency, int velocity){
		double amplitude = SAMPLE_RATE / frequency; //�g��
		for(int i = 0; i < b.length; i++){
			double r = i / amplitude;
			int v = (byte)((Math.round(r) % 2 == 0) ? velocity : -velocity);
			v += b[i];
			v = Math.min(Math.max(v,-128),127);
			b[i] = (byte)v;
		}
		//�Đ�(�o�C�g���line�ɏ�������)
		line.write(b, 0, b.length);
	}
	public void writeNoise(byte[] b, int velocity){
		for(int i = 0; i < b.length; i++){
			b[i] = (byte)Math.floor(Math.random() * velocity);
		}
	}
}