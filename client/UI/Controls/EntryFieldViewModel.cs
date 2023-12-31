﻿using CommunityToolkit.Mvvm.ComponentModel;

namespace LingoHammer.UI.Controls;

partial class EntryFieldViewModel : ObservableObject
{
    [ObservableProperty]
    private string label = string.Empty;

    [ObservableProperty]
    private string text = string.Empty;


    [ObservableProperty]
    private string errorMessage = string.Empty;

    public EntryFieldViewModel()
    {
    }
}
