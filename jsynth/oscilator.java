//Oscilator.java
package jsynth;

import javax.sound.sampled.*;

public class Oscilator{
	static int sampleRate;
	SourceDataLine line;

	public Oscilator(int sampleRate){
		this.sampleRate = sampleRate;
		//オーディオ形式を指定
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
		//再生(バイト列をlineに書き込む)
		line.write(b, 0, b.length);
		line.drain();//終了まで
	}
	/**
	 * 再生終了まで待機するdrainのラッパ
	 *
	 */
	public void drain(){
		line.drain();
	}
	/**
	 * バイト列をlineに出力するラッパ
	 */
	public void myWrite(byte[] b, int n, int length){
		line.write(b, n, length);
	}
	/**
	 * 列に周波数とバイト列の長さ指定で形を書き込む
	 *
	 */
	public void writeNote(double frequency, int sampleCount) {
		byte[] b = new byte[sampleRate];
		double amplitude = sampleRate / frequency; //波長
		for(int i = 0; i < b.length; i++){
			double r = i / amplitude;
			b[i] = (byte)((Math.round(r) % 2 == 0) ? 100 : -100);
		} //再生(バイト列をlineに書き込む)
		line.write(b, 0, b.length);
	}
	/**
	 * バイト列に周波数、ベロシティ指定で波形書き込む
	 *
	 */
	public void writeWave(byte[] b, double frequency, int velocity){
		double amplitude = sampleRate / frequency; //波長
		for(int i = 0; i < b.length; i++){
			double r = i / amplitude;
			int v = (byte)((Math.round(r) % 2 == 0) ? velocity : -velocity);
			v += b[i];
			v = Math.min(Math.max(v,-128),127);
			b[i] = (byte)v;
		}
		//再生(バイト列をlineに書き込む)
		line.write(b, 0, b.length);
	}
	public void writeNoise(byte[] b, int velocity){
		for(int i = 0; i < b.length; i++){
			b[i] = (byte)Math.floor(Math.random() * velocity);
		}
	}
}
