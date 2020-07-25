package main;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

public class Main {
	
	private Soundbank soundbank;
	private Synthesizer synthesizer;
	private MidiChannel channel;
	private Instrument instrument;
	
	private void loadInstrument(Instrument instrument) {
		this.instrument = instrument;
		synthesizer.loadInstrument(instrument);
		Patch patch = instrument.getPatch();
		channel.programChange(patch.getBank(), patch.getProgram());
		printInstrumentDetails(instrument);
	}
	
	private void loadInstrument() {
		Instrument[] instruments = soundbank.getInstruments();
		int randomIdx = ThreadLocalRandom.current().nextInt(instruments.length);
		loadInstrument(instruments[randomIdx]);
	}
	
	private void unloadAndChangeInstrument(Instrument instrument) {
		if (instrument != null) {
			synthesizer.unloadInstrument(instrument);
		}
		loadInstrument(instrument);
	}
	
	private void unloadAndChangeInstrument() {
		if (instrument != null) {
			synthesizer.unloadInstrument(instrument);
		}
		loadInstrument();
	}
	
	private static void printInstrumentDetails(Instrument instrument) {
		Patch patch = instrument.getPatch();
		System.out.println(instrument.getName() + " - (" + patch.getBank() + ", " + patch.getProgram() + ")");
	}
	
	private void exercise2() throws InterruptedException {
		int pause = 500;
		for (Instrument instrument : soundbank.getInstruments()) {
			loadInstrument(instrument);
			
			channel.noteOn(60, 64);
			Thread.sleep(pause);
			channel.noteOff(60);

			channel.noteOn(62, 64);
			Thread.sleep(pause);
			channel.noteOff(62);

			channel.noteOn(64, 64);
			Thread.sleep(pause);
			channel.noteOff(64);

			channel.noteOn(65, 64);
			Thread.sleep(pause);
			channel.noteOff(65);

			channel.noteOn(67, 64);
			Thread.sleep(pause);
			channel.noteOff(67);
			
			synthesizer.unloadInstrument(instrument);
		}
	}
	
	private void exercise1() throws InterruptedException {
		loadInstrument(soundbank.getInstrument(new Patch(0 , 5)));
		
		int pause = 500;
		
		channel.noteOn(60, 64);
		Thread.sleep(pause);
		channel.noteOff(60);

		channel.noteOn(62, 64);
		Thread.sleep(pause);
		channel.noteOff(62);

		channel.noteOn(64, 64);
		Thread.sleep(pause);
		channel.noteOff(64);

		channel.noteOn(65, 64);
		Thread.sleep(pause);
		channel.noteOff(65);
		
		unloadAndChangeInstrument();

		channel.noteOn(67, 64);
		Thread.sleep(pause);
		channel.noteOff(67);
		
		channel.noteOn(69, 64);
		Thread.sleep(pause);
		channel.noteOff(69);

		channel.noteOn(71, 64);
		Thread.sleep(pause);
		channel.noteOff(71);

		channel.noteOn(72, 64);
		Thread.sleep(pause);
		channel.noteOff(72);
	}
	
	public void main() throws MidiUnavailableException, InterruptedException, InvalidMidiDataException, IOException {
		
		soundbank = MidiSystem.getSoundbank(new File("gm.sf2"));
		
		System.out.println("Instruments");
		System.out.println("----------------");
		for (Instrument instrument : soundbank.getInstruments()) {
			printInstrumentDetails(instrument);
		}
		
		synthesizer = MidiSystem.getSynthesizer();
		if (!synthesizer.isOpen()) synthesizer.open();
		synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
		
		channel = synthesizer.getChannels()[0];
		
		exercise1();
		exercise2();
		
		Thread.sleep(1000);
		
		synthesizer.close();
	}
	
	public static void main(String[] args) throws MidiUnavailableException, InterruptedException, InvalidMidiDataException, IOException {
		new Main().main();
	}
}
