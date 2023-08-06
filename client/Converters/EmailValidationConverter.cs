using CommunityToolkit.Maui.Converters;
using System.Globalization;
using System.Text.RegularExpressions;

namespace LingoHammer.Converters
{
    public class EmailValidationConverter : BaseConverterOneWay<string?, bool>
    {
        public override bool DefaultConvertReturnValue { get; set; } = false;

        internal static bool IsEmailValid(string email)
        {
            //validate that value is a valid email address
            if (string.IsNullOrEmpty(email))
            {
                return false;
            }

            if (!email.Contains('@', StringComparison.CurrentCulture))
            {
                return false;
            }
            if (!email.Contains('.', StringComparison.CurrentCulture))
            {
                return false;
            }
            try
            {
                // Normalize the domain
                email = Regex.Replace(email, @"(@)(.+)$", DomainMapper,
                                      RegexOptions.None, TimeSpan.FromMilliseconds(200));

                // Examines the domain part of the email and normalizes it.
                string DomainMapper(Match match)
                {
                    // Use IdnMapping class to convert Unicode domain names.
                    var idn = new IdnMapping();

                    // Pull out and process domain name (throws ArgumentException on invalid)
                    string domainName = idn.GetAscii(match.Groups[2].Value);

                    return match.Groups[1].Value + domainName;
                }
            }
            catch (RegexMatchTimeoutException)
            {
                return false;
            }
            catch (ArgumentException)
            {
                return false;
            }

            try
            {
                return Regex.IsMatch(email,
                    @"^[^@\s]+@[^@\s]+\.[^@\s]+$",
                    RegexOptions.IgnoreCase, TimeSpan.FromMilliseconds(100));
            }
            catch (RegexMatchTimeoutException)
            {
                return false;
            }
        }

        public override bool ConvertFrom(string email, CultureInfo culture)
        {
            return IsEmailValid(email);

        }
    }
}
