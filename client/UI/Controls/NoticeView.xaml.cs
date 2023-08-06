using CommunityToolkit.Mvvm.Input;
using System.Runtime.CompilerServices;
using System.Windows.Input;

namespace LingoHammer.UI.Controls;

public partial class NoticeView
{
    #region Message : string
    public static readonly BindableProperty MessageProperty =
    BindableProperty.Create(nameof(Message),
        typeof(string),
        typeof(NoticeView));

    public string Message
    {
        get => (string)GetValue(MessageProperty);
        set => SetValue(MessageProperty, value);
    }
    #endregion

    #region NoticeType : NoticeViewType
    public static readonly BindableProperty NoticeTypeProperty =
    BindableProperty.Create(nameof(NoticeType),
        typeof(NoticeType),
        typeof(NoticeView), NoticeType.Information);
    public NoticeType NoticeType
    {
        get => (NoticeType)GetValue(NoticeTypeProperty);
        set => SetValue(NoticeTypeProperty, value);
    }
    #endregion

    #region ClickCommand : ICommand
    public static readonly BindableProperty ClickCommandProperty =
    BindableProperty.Create(nameof(ClickCommand),
        typeof(ICommand),
        typeof(NoticeView));
    public ICommand ClickCommand
    {
        get => (ICommand)GetValue(ClickCommandProperty);
        set => SetValue(ClickCommandProperty, value);
    }
    #endregion

    #region CloseCommand : ICommand
    public static readonly BindableProperty CloseCommandProperty =
    BindableProperty.Create(nameof(CloseCommand),
        typeof(ICommand),
        typeof(NoticeView));
    public ICommand CloseCommand
    {
        get => (ICommand)GetValue(CloseCommandProperty);
        set => SetValue(CloseCommandProperty, value);
    }
    #endregion

    #region NoticeBorderColor : Color
    public static readonly BindableProperty NoticeBorderColorProperty =
    BindableProperty.Create(nameof(NoticeBorderColor),
        typeof(Color),
        typeof(NoticeView));
    public Color NoticeBorderColor
    {
        get => (Color)GetValue(NoticeBorderColorProperty);
        set => SetValue(NoticeBorderColorProperty, value);
    }
    #endregion

    #region NoticeBackgroundColor : Color
    public static readonly BindableProperty NoticeBackgroundColorProperty =
    BindableProperty.Create(nameof(NoticeBackgroundColor),
        typeof(Color),
        typeof(NoticeView));
    public Color NoticeBackgroundColor
    {
        get => (Color)GetValue(NoticeBackgroundColorProperty);
        set => SetValue(NoticeBackgroundColorProperty, value);
    }
    #endregion

    #region NoticeTextColor : Color
    public static readonly BindableProperty NoticeTextColorProperty =
    BindableProperty.Create(nameof(NoticeTextColor),
        typeof(Color),
        typeof(NoticeView));
    public Color NoticeTextColor
    {
        get => (Color)GetValue(NoticeTextColorProperty);
        set => SetValue(NoticeTextColorProperty, value);
    }
    #endregion

    #region HasClose : bool
    public static readonly BindableProperty HasCloseProperty =
    BindableProperty.Create(nameof(HasClose),
        typeof(bool),
        typeof(NoticeView), true);
    public bool HasClose
    {
        get => (bool)GetValue(HasCloseProperty);
        set => SetValue(HasCloseProperty, value);
    }
    #endregion

    private NoticeViewModel ViewModel => BindingContainer?.BindingContext as NoticeViewModel;
    public NoticeView()
    {
        InitializeComponent();

        IsVisible = false;
        ViewModel.CloseCommand = new RelayCommand(() => IsVisible = false);
        SetDynamicResource(StyleProperty, "NoticeTypeStyle");

        //get style from resources
        Resources["NoticeTypeStyle"] = StyleForType(NoticeType);
    }

    protected override void OnPropertyChanged([CallerMemberName] string propertyName = null)
    {
        base.OnPropertyChanged(propertyName);

        if (propertyName == MessageProperty.PropertyName)
        {
            ViewModel.Message = Message;
            IsVisible = !string.IsNullOrEmpty(Message);
        }

        if (propertyName == ClickCommandProperty.PropertyName)
        {
            ViewModel.ClickCommand = ClickCommand;
        }

        if (propertyName == CloseCommandProperty.PropertyName)
        {
            ViewModel.CloseCommand = CloseCommand;
        }

        if (propertyName == NoticeBorderColorProperty.PropertyName)
        {
            ViewModel.NoticeBorderColor = NoticeBorderColor;
        }

        if (propertyName == NoticeBackgroundColorProperty.PropertyName)
        {
            ViewModel.NoticeBackgroundColor = NoticeBackgroundColor;
        }

        if (propertyName == NoticeTextColorProperty.PropertyName)
        {
            ViewModel.NoticeTextColor = NoticeTextColor;
        }

        if (propertyName == HasCloseProperty.PropertyName)
        {
            ViewModel.HasClose = HasClose;
        }


        if (propertyName == NoticeTypeProperty.PropertyName)
        {
            Resources["NoticeTypeStyle"] = StyleForType(NoticeType);
            IsVisible = !string.IsNullOrEmpty(Message);
        }
    }

    private Style StyleForType(NoticeType type)
    {
        var styleName = $"Notice{type}Style";
        Application.Current.Resources.TryGetValue(styleName, out var style);
        return style as Style;
    }
}