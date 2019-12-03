package com.dtdennis.currency.ui

class MainVMTest {
    // Upon first observe, should emit a conversion list
    // with the default baseline

    // Upon observe, should emit a CL
    // with the currency line items matching true conversions

    // Upon observe, should emit a CL
    // with the CLIs matching supported currency names & icons

    // Upon observe, should emit a CL
    // that matches the previous positions of the currencies
    // Baseline positions should be reflected in CL lineup

    // Upon observe, and then baseline changed
    // Should emit a new CL reflecting the new BL

    // Upon observe, then baseline changed, then destruction
    // And then re-observe
    // Should emit a CL reflecting the persisted BL

    // FUTURE
    // Upon observe, should emit a CL
    // that matches previous positions,
    // and adds new currencies at bottom
}