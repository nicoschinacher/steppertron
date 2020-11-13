package com.company;

import com.company.events.Tick;
import com.company.events.TickNote;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.util.ArrayList;

public class SteppertronSimulation {

    private final ArrayList<Tick> ticks;
    private final MidiChannel[] midiChannels;

    public SteppertronSimulation(ArrayList<Tick> ticks) throws MidiUnavailableException {
        this.ticks = ticks;
        // initialize synthesizer and channels
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        this.midiChannels = synthesizer.getChannels();
    }
    public void play() throws InterruptedException {
        long count = 0;
        long maxCount = ticks.get(ticks.size() - 1).getNumber();
        while (count < maxCount) {
            for(Tick tick : ticks) {
                if(tick.getNumber() > count) {
                    break;
                }
                if(tick instanceof TickNote
                    && tick.getNumber() == count) {
                    TickNote tickNote = (TickNote) tick;
                    playNote(tickNote.getGeneralNote(), tickNote.getOn(), tickNote.getVelocity());
                }
            }
            // TODO: calculate delta time (PPQ / BPM / Tick)
            count += 1;
        }
    }
    private void playNote(int note, boolean on, int velocity) {
        if(on) {
            midiChannels[0].noteOn(note, velocity);
        } else {
            midiChannels[0].noteOff(note);
        }
    }
}