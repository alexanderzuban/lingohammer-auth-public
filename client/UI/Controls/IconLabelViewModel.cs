using CommunityToolkit.Mvvm.ComponentModel;
using Microsoft.Maui.Controls.Shapes;
using System.Windows.Input;

namespace LingoHammer.UI.Controls;

partial class IconLabelViewModel : ObservableObject
{
    [ObservableProperty]
    private string icon = string.Empty;

    [ObservableProperty]
    private ICommand command;

    [ObservableProperty]
    private bool hasBorder;

    [ObservableProperty]
    private Color iconColor = Color.FromArgb("#404040");

    [ObservableProperty]
    private Color backgroundColor;

    [ObservableProperty]
    private Color borderColor;

    [ObservableProperty]
    private double borderWidth = 0.5;

    [ObservableProperty]
    private double iconSize = 24;

    [ObservableProperty]
    private int borderRadius = 5;

    [ObservableProperty]
    private RoundRectangle borderShape;


    partial void OnBorderRadiusChanged(int value)
    {
        if (value < 0)
        {
            BorderShape = null;
        }
        else
        {
            BorderShape = new RoundRectangle
            {
                CornerRadius = new CornerRadius(BorderRadius)
            };
        }


    }


}
