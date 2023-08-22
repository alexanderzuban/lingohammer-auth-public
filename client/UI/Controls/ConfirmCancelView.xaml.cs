using System.Runtime.CompilerServices;
using System.Windows.Input;

namespace LingoHammer.UI.Controls;

public partial class ConfirmCancelView
{
    //bindable properties for the view
    #region CancelCommand
    public static readonly BindableProperty CancelCommandProperty = BindableProperty.Create(
        nameof(CancelCommand), typeof(ICommand), typeof(ConfirmCancelView), null);
    public ICommand CancelCommand
    {
        get => (ICommand)GetValue(CancelCommandProperty);
        set => SetValue(CancelCommandProperty, value);
    }
    #endregion

    #region ConfirmCommand
    public static readonly BindableProperty ConfirmCommandProperty = BindableProperty.Create(
        nameof(ConfirmCommand), typeof(ICommand), typeof(ConfirmCancelView), null);
    public ICommand ConfirmCommand
    {
        get => (ICommand)GetValue(ConfirmCommandProperty);
        set => SetValue(ConfirmCommandProperty, value);
    }
    #endregion

    #region CancelText
    public static readonly BindableProperty CancelTextProperty = BindableProperty.Create(
        nameof(CancelText), typeof(string), typeof(ConfirmCancelView), "Cancel");
    public string CancelText
    {
        get => (string)GetValue(CancelTextProperty);
        set => SetValue(CancelTextProperty, value);
    }
    #endregion

    #region ConfirmText
    public static readonly BindableProperty ConfirmTextProperty = BindableProperty.Create(
        nameof(ConfirmText), typeof(string), typeof(ConfirmCancelView), "Ok");
    public string ConfirmText
    {
        get => (string)GetValue(ConfirmTextProperty);
        set => SetValue(ConfirmTextProperty, value);
    }
    #endregion

    #region CanConfirm
    public static readonly BindableProperty CanConfirmProperty = BindableProperty.Create(
        nameof(CanConfirm), typeof(bool), typeof(ConfirmCancelView), true);
    public bool CanConfirm
    {
        get => (bool)GetValue(CanConfirmProperty);
        set => SetValue(CanConfirmProperty, value);
    }
    #endregion  

    #region CanCancel
    public static readonly BindableProperty CanCancelProperty = BindableProperty.Create(
        nameof(CanCancel), typeof(bool), typeof(ConfirmCancelView), true);
    public bool CanCancel
    {
        get => (bool)GetValue(CanCancelProperty);
        set => SetValue(CanCancelProperty, value);
    }
    #endregion

    //interal view model
    private ConfirmCancelViewModel ViewModel =>
        BindingContainer?.BindingContext as ConfirmCancelViewModel;

    public ConfirmCancelView()
    {
        InitializeComponent();
    }


    //when the binding context changes, update the view model
    protected override void OnPropertyChanged([CallerMemberName] string propertyName = null)
    {
        base.OnPropertyChanged(propertyName);

        if (ViewModel is not null)
        {
            ViewModel.CancelCommand = CancelCommand;
            ViewModel.ConfirmCommand = ConfirmCommand;
            ViewModel.CancelText = CancelText;
            ViewModel.ConfirmText = ConfirmText;
            ViewModel.CanConfirm = CanConfirm;
            ViewModel.CanCancel = CanCancel;
        }
    }


}