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

	static int getNoteNo(int octave, int noteName){
		return octave * 12 + noteName;
	}

	public void showName(){
		System.out.println("package JSynth");
	}
}

