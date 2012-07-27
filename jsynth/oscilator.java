//Oscilator.java
package jsynth;

import javax.sound.sampled.*;

public class Oscilator{
	static int sampleRate;
	SourceDataLine line;

	public Oscilator(int sampleRate){
		this.sampleRate = sampleRate;
		//�I�[�f�B�I�`�����w��
		AudioFormat af = new AudioFormat(
			sampleRate, 8, 1, true, true);
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

	public void writeNote(double frequency) {
		byte[] b = new byte[sampleRate];
		for(int i = 0; i < b.length; i++){
			double r = i / (sampleRate / frequency);
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
	 * �o�C�g���line�ɏo�͂��郉�b�p
	 */
	public void myWrite(byte[] b, int n, int length){
		line.write(b, n, length);
	}
	/**
	 * ��Ɏ��g���ƃo�C�g��̒����w��Ō`����������
	 *
	 */
	public void writeNote(double frequency, int sampleCount) {
		byte[] b = new byte[sampleRate];
		double amplitude = sampleRate / frequency; //�g��
		for(int i = 0; i < b.length; i++){
			double r = i / amplitude;
			b[i] = (byte)((Math.round(r) % 2 == 0) ? 100 : -100);
		} //�Đ�(�o�C�g���line�ɏ�������)
		line.write(b, 0, b.length);
	}
	/**
	 * �o�C�g��Ɏ��g���A�x���V�e�B�w��Ŕg�`��������
	 *
	 */
	public void writeWave(byte[] b, double frequency, int velocity){
		double amplitude = sampleRate / frequency; //�g��
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
