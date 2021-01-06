package validation;

import org.apache.commons.cli.*;

import java.util.HashSet;
import java.util.Set;

public class ApacheValidator {

    private static final String IP_REGEX = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$";
    private static final String FILENAME_REGEX = "[_\\-\\.0-9a-z]{1,127}";
    private static final String ACCOUNT_NAME_REGEX = "[_\\-\\.0-9a-z]{1,122}";
    private static final String AMOUNT_REGEX = "(0|[1-9][0-9]*)\\.[0-9][0-9]";

    private static Options prepareAtmOptions() {
        Options options = new Options();

        Option authFileOption = Option.builder(Constants.AUTH_FILE_OPTION).required(false).hasArg().type(String.class).build();
        Option ipOption = Option.builder(Constants.IP_OPTION).required(false).hasArg().type(String.class).build();
        Option portOption = Option.builder(Constants.PORT_OPTION).required(false).hasArg().type(Integer.class).build();
        Option cardOption = Option.builder(Constants.CARD_FILE_OPTION).required(false).hasArg().type(String.class).build();
        Option accountNameOption = Option.builder(Constants.ACCOUNT_OPTION).required().hasArg().type(String.class).build();

        OptionGroup actionCodeOption = new OptionGroup();
        Option newActionOption = Option.builder(Constants.NEW_OPTION).required(false).hasArg(false).build();
        Option withdrawActionOption = Option.builder(Constants.WITHDRAW_OPTION).required(false).hasArg(false).build();
        Option depositActionOption = Option.builder(Constants.DEPOSIT_OPTION).required(false).hasArg(false).build();
        Option balanceActionOption = Option.builder(Constants.GET_BALANCE_OPTION).required(false).hasArg(false).build();
        actionCodeOption.setRequired(true);
        actionCodeOption.addOption(newActionOption).addOption(withdrawActionOption).addOption(depositActionOption).addOption(balanceActionOption);

        options.addOption(authFileOption).addOption(ipOption).addOption(portOption).addOption(cardOption)
                .addOption(accountNameOption).addOptionGroup(actionCodeOption);
        return options;
    }

    private static Options prepareBankOptions() {
        Options options = new Options();

        Option authFileOption = Option.builder(Constants.AUTH_FILE_OPTION).required(false).hasArg().type(String.class).build();
        Option portOption = Option.builder(Constants.PORT_OPTION).required(false).hasArg().type(Integer.class).build();

        options.addOption(authFileOption).addOption(portOption);
        return options;
    }

    public static BankArgs validateBankArgs(String[] args) {
        BankArgs response = new BankArgs();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine parse = parser.parse(prepareBankOptions(), args, false);

            if(!validateDuplicates(parse))
                return invalidBankResponse();

            String authFile = parse.getOptionValue(Constants.AUTH_FILE_OPTION, "bank.auth");
            String port = parse.getOptionValue(Constants.PORT_OPTION, "3000");

            if(parse.getArgs().length != 0)
                return invalidBankResponse();

            if(isEmpty(authFile))
                return invalidBankResponse();

            // Max length 4096 characters
            if(!isEmpty(authFile) && isArgInvalidLength(authFile))
                return invalidBankResponse();

            // Auth file
            if(!isValidFilename(authFile))
                return invalidBankResponse();

            // Port
            int portVal = validateAndGetPort(port);

            // All good
            response.setValid(true);
            response.setReturnCode(0);
            response.setAuthFile(authFile);
            response.setPort(portVal);
        }
        catch (ParseException e)
        {
            response.setReturnCode(255);
            response.setValid(false);
        }

        return response;
    }

    public static AtmArgs validateAtmArgs(String[] args) {
        AtmArgs response = new AtmArgs();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine parse = parser.parse(prepareAtmOptions(), args, false);

            if(!validateDuplicates(parse))
                return invalidAtmResponse();

            String authFile = parse.getOptionValue(Constants.AUTH_FILE_OPTION, "bank.auth");
            String ip = parse.getOptionValue(Constants.IP_OPTION, "127.0.0.1");
            String port = parse.getOptionValue(Constants.PORT_OPTION, "3000");
            String accountName = parse.getOptionValue(Constants.ACCOUNT_OPTION);
            String cardFile = parse.getOptionValue(Constants.CARD_FILE_OPTION);

            if(isEmpty(authFile) || isEmpty(ip) || isEmpty(accountName))
                return invalidAtmResponse();

            // Max length 4096 characters
            if(isArgInvalidLength(accountName)
                    || (!isEmpty(cardFile) && isArgInvalidLength(cardFile))
                    || (!isEmpty(ip) && isArgInvalidLength(ip))
                    || (!isEmpty(authFile) && isArgInvalidLength(authFile)))
                return invalidAtmResponse();

            // Account name
            if(!isValidAccountName(accountName))
                return invalidAtmResponse();
            // Auth file
            if(!isValidFilename(authFile))
                return invalidAtmResponse();
            // Card file
            if(!isEmpty(cardFile) && !isValidFilename(cardFile))
                return invalidAtmResponse();

            // IP
            if(!isValidIp(ip))
                return invalidAtmResponse();

            // Port
            int portVal = validateAndGetPort(port);

            // Action code
            AtmArgs.ActionCode actionCode = null;
            if(parse.hasOption(Constants.NEW_OPTION)) actionCode = AtmArgs.ActionCode.NEW;
            if(parse.hasOption(Constants.WITHDRAW_OPTION)) actionCode = AtmArgs.ActionCode.WITHDRAW;
            if(parse.hasOption(Constants.DEPOSIT_OPTION)) actionCode = AtmArgs.ActionCode.DEPOSIT;
            if(parse.hasOption(Constants.GET_BALANCE_OPTION)) actionCode = AtmArgs.ActionCode.BALANCE;


            // Amount
            String[] parseArgs = parse.getArgs();
            double amount = 0;
            if(AtmArgs.ActionCode.BALANCE.equals(actionCode)) {
                if(parseArgs.length > 0)
                    return invalidAtmResponse();
            }
            else{
                if(parseArgs.length != 1)
                    return invalidAtmResponse();

                if (isEmpty(parseArgs[0]))
                    return invalidAtmResponse();

                amount = validateAndGetAmount(parseArgs[0]);
            }

            // All good
            response.setValid(true);
            response.setReturnCode(0);
            response.setAuthFile(authFile);
            response.setAccountName(accountName);
            if(isEmpty(cardFile)) response.setCardFile(accountName + ".card"); else response.setCardFile(cardFile);
            response.setIp(ip);
            response.setPort(portVal);
            response.setActionCode(actionCode);
            response.setAmount(amount);
        }
        catch (ParseException e)
        {
            response.setReturnCode(255);
            response.setValid(false);
        }

        return response;
    }

    private static boolean validateDuplicates(CommandLine parse) {
        Option[] options = parse.getOptions();
        Set<String> providedArgs = new HashSet<>(options.length);

        for(Option option : options)
            providedArgs.add(option.getOpt());

        return options.length == providedArgs.size();
    }

    public static double validateAndGetAmount(String amount) throws ParseException
    {
        if(!amount.matches(AMOUNT_REGEX))
            throw new ParseException("Invalid amount");

        double amountVal = Double.parseDouble(amount);

        if (amountVal <= 0 || amountVal > 4294967295.99)
            throw new ParseException("Invalid amount");

        return amountVal;
    }

    public static boolean isValidAccountName(String accountName) {
        if(isEmpty(accountName))
            return false;

        return accountName.matches(ACCOUNT_NAME_REGEX);
    }

    public static boolean isValidFilename(String filename) {
        if(isEmpty(filename))
            return false;

        if(".".equals(filename) || "..".equals(filename))
            return false;

        return filename.matches(FILENAME_REGEX);
    }

    public static int validateAndGetPort(String port) throws ParseException {
        if(port.startsWith("0"))
            throw new ParseException("Invalid port");

        int portVal;
        try {
            portVal = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid port");
        }

        if(portVal < 1024 || portVal > 65535)
            throw new ParseException("Invalid port");

        return portVal;
    }

    public static boolean isValidIp(String ip)
    {
        if(isEmpty(ip))
            return false;

        if(!ip.matches(IP_REGEX))
            return false;

        String[] portions = ip.split("\\.");
        for(String portion : portions)
        {
            if(!isValidOctet(portion))
                return false;
        }

        return true;
    }

    public static boolean isValidOctet(String octet) {
        if(octet.length() > 1 && octet.startsWith("0"))
            return false;

        try {
            int value = Integer.parseInt(octet);
            if(value > 255)
                return false;
        }
        catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static boolean isEmpty(String str)
    {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isArgInvalidLength(String arg)
    {
        return arg.length() > 4096;
    }

    public static AtmArgs invalidAtmResponse()
    {
        AtmArgs response = new AtmArgs();
        response.setValid(false);
        response.setReturnCode(255);
        return response;
    }


    public static BankArgs invalidBankResponse()
    {
        BankArgs response = new BankArgs();
        response.setValid(false);
        response.setReturnCode(255);
        return response;
    }

}
