using System.Windows.Input;

namespace LingoHammer.UI.Controls;

public partial class IconLabelView
{
    //command to execute when the icon is clicked
    #region Command
    public static readonly BindableProperty CommandProperty = BindableProperty.Create(
        nameof(Command), typeof(ICommand), typeof(IconLabelView), default(ICommand), BindingMode.OneWay, null, OnCommandPropertyChanged
    );
    public ICommand Command
    {
        get => (ICommand)GetValue(CommandProperty);
        set => SetValue(CommandProperty, value);
    }

    private static void OnCommandPropertyChanged(BindableObject bindable, object oldValue, object newValue)
    {
        if ((bindable as IconLabelView)?.ViewModel is IconLabelViewModel model)
        {
            model.Command = (ICommand)newValue;
        }
    }
    #endregion

    //icon bindable property
    #region Icon
    public static readonly BindableProperty IconProperty = BindableProperty.Create(
               nameof(Icon), typeof(string), typeof(IconLabelView), default(string), BindingMode.TwoWay, null, OnIconChanged
           );
    public string Icon
    {
        get => (string)GetValue(IconProperty);
        set => SetValue(IconProperty, value);
    }
    private static void OnIconChanged(BindableObject bindable, object oldValue, object newValue)
    {
        if ((bindable as IconLabelView)?.ViewModel is IconLabelViewModel model)
        {
            model.Icon = (string)newValue;
        }
    }
    #endregion

    //border color bindable property
    #region BackgroundColor
    public static readonly BindableProperty BorderColorProperty = BindableProperty.Create(
                      nameof(BorderColor), typeof(Color), typeof(IconLabelView), default(Color), BindingMode.TwoWay, null, OnBorderColorChanged
                  );
    public Color BorderColor
    {
        get => (Color)GetValue(BorderColorProperty);
        set => SetValue(BorderColorProperty, value);
    }
    private static void OnBorderColorChanged(BindableObject bindable, object oldValue, object newValue)
    {
        if ((bindable as IconLabelView)?.ViewModel is IconLabelViewModel model)
        {
            model.BorderColor = (Color)newValue;
        }
    }
    #endregion

    //border width bindable property
    #region BorderWidth
    public static readonly BindableProperty BorderWidthProperty = BindableProperty.Create(
                             nameof(BorderWidth), typeof(double), typeof(IconLabelView), default(double), BindingMode.TwoWay, null, OnBorderWidthChanged
                         );
    public double BorderWidth
    {
        get => (double)GetValue(BorderWidthProperty);
        set => SetValue(BorderWidthProperty, value);
    }
    private static void OnBorderWidthChanged(BindableObject bindable, object oldValue, object newValue)
    {
        if ((bindable as IconLabelView)?.ViewModel is IconLabelViewModel model)
        {
            model.BorderWidth = (double)newValue;
        }
    }
    #endregion

    //bindable property for the icon size
    #region IconSize
    public static readonly BindableProperty IconSizeProperty = BindableProperty.Create(
               nameof(IconSize), typeof(double), typeof(IconLabelView), default(double), BindingMode.TwoWay, null, OnIconSizeChanged
           );
    public double IconSize
    {
        get => (double)GetValue(IconSizeProperty);
        set => SetValue(IconSizeProperty, value);
    }
    private static void OnIconSizeChanged(BindableObject bindable, object oldValue, object newValue)
    {
        if ((bindable as IconLabelView)?.ViewModel is IconLabelViewModel model)
        {
            model.IconSize = (double)newValue;
        }
    }
    #endregion

    //bindable property for the border radius
    #region BorderRadius
    public static readonly BindableProperty BorderRadiusProperty = BindableProperty.Create(
                      nameof(BorderRadius), typeof(int), typeof(IconLabelView), default(int), BindingMode.TwoWay, null, OnBorderRadiusChanged
                  );
    public int BorderRadius
    {
        get => (int)GetValue(BorderRadiusProperty);
        set => SetValue(BorderRadiusProperty, value);
    }
    private static void OnBorderRadiusChanged(BindableObject bindable, object oldValue, object newValue)
    {
        if ((bindable as IconLabelView)?.ViewModel is IconLabelViewModel model)
        {
            model.BorderRadius = (int)newValue;
        }
    }
    #endregion

    //bindable property for the icon color
    #region IconColor
    public static readonly BindableProperty IconColorProperty = BindableProperty.Create(
                      nameof(IconColor), typeof(Color), typeof(IconLabelView), default(Color), BindingMode.TwoWay, null, OnIconColorChanged
                  );
    public Color IconColor
    {
        get => (Color)GetValue(IconColorProperty);
        set => SetValue(IconColorProperty, value);
    }
    private static void OnIconColorChanged(BindableObject bindable, object oldValue, object newValue)
    {
        if ((bindable as IconLabelView)?.ViewModel is IconLabelViewModel model)
        {
            model.IconColor = (Color)newValue;
        }
    }
    #endregion



    private IconLabelViewModel ViewModel => BindingContainer?.BindingContext as IconLabelViewModel;

    public IconLabelView()
    {
        InitializeComponent();
    }
}