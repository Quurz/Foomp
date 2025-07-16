package org.quurz.foomp.automata;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface MachineListener<S, IA, OA> {

    void stateTransitionEvent(final MachineEvent.@NonNull MachineStateTransitionEvent<S, IA, OA> machineStateTransitionEvent);

    void errorEvent(final MachineEvent.@NonNull MachineErrorEvent<S, IA, OA> errorEvent);

}
