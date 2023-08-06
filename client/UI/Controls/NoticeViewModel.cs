using CommunityToolkit.Mvvm.ComponentModel;
using System.Windows.Input;

namespace LingoHammer.UI.Controls
{
    partial class NoticeViewModel : ObservableObject
    {
        [ObservableProperty]
        private string message = string.Empty;

        [ObservableProperty]
        private NoticeType noticeType = NoticeType.Information;


        [ObservableProperty]
        private Color noticeBackgroundColor;

        [ObservableProperty]
        private Color noticeTextColor;

        [ObservableProperty]
        private Color noticeBorderColor;

        [ObservableProperty]
        private ICommand clickCommand;

        [ObservableProperty]
        private ICommand closeCommand;

        [ObservableProperty]
        private bool hasClose = true;

    }


    public enum NoticeType
    {
        Information,
        Success,
        Warning,
        Error
    }
}
