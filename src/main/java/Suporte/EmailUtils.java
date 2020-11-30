package Suporte;

import javax.mail.*;
import javax.mail.search.SubjectTerm;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {
    private Folder folder;

    public enum EmailFolder {
        INBOX("INBOX");


        private String text;

        private EmailFolder(String text){
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    /**
     * Uses email.username and email.password properties from the properties file. Reads from Inbox folder of the email application
     * @throws MessagingException
     */
    public EmailUtils() throws MessagingException {
        this(EmailFolder.INBOX);
    }

    /**
     * Uses username and password in properties file to read from a given folder of the email application
     * @param emailFolder Folder in email application to interact with
     * @throws MessagingException
     */
    public EmailUtils(EmailFolder emailFolder) throws MessagingException {
        this(getEmailUsernameFromProperties(),
                getEmailPasswordFromProperties(),
                getEmailServerFromProperties(),
                emailFolder);
    }



    public EmailUtils(String username, String password, String server, EmailFolder emailFolder) throws MessagingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.auth",true);
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.transport.protocol","smtp");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.port","465");

        Session session = Session.getInstance(props);
        Store store = session.getStore("imaps");
        store.connect(server, username, password);


        folder = store.getFolder(emailFolder.getText());
        folder.open(Folder.READ_WRITE);
    }



    //************* GET EMAIL PROPERTIES *******************

    public static String getEmailAddressFromProperties(){
        return System.getProperty("email.address");
    }

    public static String getEmailUsernameFromProperties(){
        return System.getProperty("email.username");
    }

    public static String getEmailPasswordFromProperties(){
        return System.getProperty("email.password");
    }

    public static String getEmailProtocolFromProperties(){
        return System.getProperty("email.protocol");
    }

    public static int getEmailPortFromProperties(){
        return Integer.parseInt(System.getProperty("email.port"));
    }

    public static String getEmailServerFromProperties(){
        return System.getProperty("email.server");
    }




    //************* EMAIL ACTIONS *******************

    public void openEmail(Message message) throws Exception {
        message.getContent();
    }

    public int getNumberOfMessages() throws MessagingException {
        return folder.getMessageCount();
    }

    public int getNumberOfUnreadMessages()throws MessagingException {
        return folder.getUnreadMessageCount();
    }

    /**
     * Gets a message by its position in the folder. The earliest message is indexed at 1.
     */
    public Message getMessageByIndex(int index) throws MessagingException {
        return folder.getMessage(index);
    }

    public Message getLatestMessage() throws MessagingException{
        return getMessageByIndex(getNumberOfMessages());
    }

    /**
     * Gets all messages within the folder
     */
    public Message[] getAllMessages() throws MessagingException {
        return folder.getMessages();
    }

    /**
     * @param maxToGet maximum number of messages to get, starting from the latest. For example, enter 100 to get the last 100 messages received.
     */
    public Message[] getMessages(int maxToGet) throws MessagingException {
        Map<String, Integer> indices = getStartAndEndIndices(maxToGet);
        return folder.getMessages(indices.get("startIndex"), indices.get("endIndex"));
    }

    /**
     * Searches for messages with a specific subject
     * @param subject Subject to search messages for
     * @param unreadOnly Indicate whether to only return matched messages that are unread
     * @param maxToSearch maximum number of messages to search, starting from the latest. For example, enter 100 to search through the last 100 messages.
     */
    public Message[] getMessagesBySubject(String subject, boolean unreadOnly, int maxToSearch) throws Exception {
        Map<String, Integer> indices = getStartAndEndIndices(maxToSearch);

        Message messages[] = folder.search(
                new SubjectTerm(subject),
                folder.getMessages(indices.get("startIndex"), indices.get("endIndex")));

        if(unreadOnly){
            List<Message> unreadMessages = new ArrayList<Message>();
            for (Message message : messages) {
                if(isMessageUnread(message)) {
                    unreadMessages.add(message);
                }
            }
            messages = unreadMessages.toArray(new Message[]{});
        }

        return messages;
    }

    /**
     * Returns HTML of the email's content
     */
    public String getMessageContent(Message message) throws Exception {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(message.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }

    /**
     * Returns all urls from an email message with the linkText specified
     */
    public List<String> getUrlsFromMessage(Message message, String linkText) throws Exception {
        String html = getMessageContent(message);
        System.out.println(html);
        List<String> allMatches = new ArrayList<String>();
        Matcher matcher = Pattern.compile("(<a[^>]+>)"+linkText+"</a>").matcher(html);
        while (matcher.find()) {
            String aTag = matcher.group(1);
            allMatches.add(aTag.substring(aTag.indexOf("http"), aTag.indexOf("\""+" ")));
        }
        return allMatches;

    }
    //<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html  xmlns="http://www.w3.org/1999/xhtml"  xmlns:o="urn:schemas-microsoft-com:office:office"  style="    width: 100%;    font-family: roboto, 'helvetica neue', helvetica, arial, sans-serif;    -webkit-text-size-adjust: 100%;    -ms-text-size-adjust: 100%;    padding: 0;    margin: 0;  ">  <head>    <meta charset="UTF-8" />    <meta content="width=device-width, initial-scale=1" name="viewport" />    <meta name="x-apple-disable-message-reformatting" />    <meta http-equiv="X-UA-Compatible" content="IE=edge" />    <meta content="telephone=no" name="format-detection" />    <title>Novo e-mail</title>    <!--[if (mso 16)      ]><style type="text/css">        a {          text-decoration: none;        }      </style><!    [endif]-->    <!--[if gte mso 9      ]><style>        sup {          font-size: 100% !important;        }      </style><!    [endif]-->    <!--[if gte mso 9      ]><xml>        <o:OfficeDocumentSettings>          <o:AllowPNG></o:AllowPNG> <o:PixelsPerInch>96</o:PixelsPerInch>        </o:OfficeDocumentSettings>      </xml><!    [endif]-->    <!--[if !mso]><!-- -->    <link      href="https://fonts.googleapis.com/css?family=Roboto:400,400i,700,700i"      rel="stylesheet"    />    <!--<![endif]-->    <style type="text/css">      #outlook a {        padding: 0;      }      .ExternalClass {        width: 100%;      }      .ExternalClass,      .ExternalClass p,      .ExternalClass span,      .ExternalClass font,      .ExternalClass td,      .ExternalClass div {        line-height: 100%;      }      .es-button {        mso-style-priority: 100 !important;        text-decoration: none !important;      }      a[x-apple-data-detectors] {        color: inherit !important;        text-decoration: none !important;        font-size: inherit !important;        font-family: inherit !important;        font-weight: inherit !important;        line-height: inherit !important;      }      .es-desk-hidden {        display: none;        float: left;        overflow: hidden;        width: 0;        max-height: 0;        line-height: 0;        mso-hide: all;      }      @media only screen and (max-width: 600px) {        p,        ul li,        ol li,        a {          font-size: 16px !important;          line-height: 150% !important;        }        h1 {          font-size: 30px !important;          text-align: center;          line-height: 120% !important;        }        h2 {          font-size: 26px !important;          text-align: center;          line-height: 120% !important;        }        h3 {          font-size: 20px !important;          text-align: center;          line-height: 120% !important;        }        h1 a {          font-size: 30px !important;        }        h2 a {          font-size: 26px !important;        }        h3 a {          font-size: 20px !important;        }        .es-menu td a {          font-size: 16px !important;        }        .es-header-body p,        .es-header-body ul li,        .es-header-body ol li,        .es-header-body a {          font-size: 16px !important;        }        .es-footer-body p,        .es-footer-body ul li,        .es-footer-body ol li,        .es-footer-body a {          font-size: 16px !important;        }        .es-infoblock p,        .es-infoblock ul li,        .es-infoblock ol li,        .es-infoblock a {          font-size: 12px !important;        }        *[class="gmail-fix"] {          display: none !important;        }        .es-m-txt-c,        .es-m-txt-c h1,        .es-m-txt-c h2,        .es-m-txt-c h3 {          text-align: center !important;        }        .es-m-txt-r,        .es-m-txt-r h1,        .es-m-txt-r h2,        .es-m-txt-r h3 {          text-align: right !important;        }        .es-m-txt-l,        .es-m-txt-l h1,        .es-m-txt-l h2,        .es-m-txt-l h3 {          text-align: left !important;        }        .es-m-txt-r img,        .es-m-txt-c img,        .es-m-txt-l img {          display: inline !important;        }        .es-button-border {          display: block !important;        }        a.es-button {          font-size: 20px !important;          display: block !important;          border-width: 10px 0px 10px 0px !important;        }        .es-btn-fw {          border-width: 10px 0px !important;          text-align: center !important;        }        .es-adaptive table,        .es-btn-fw,        .es-btn-fw-brdr,        .es-left,        .es-right {          width: 100% !important;        }        .es-content table,        .es-header table,        .es-footer table,        .es-content,        .es-footer,        .es-header {          width: 100% !important;          max-width: 600px !important;        }        .es-adapt-td {          display: block !important;          width: 100% !important;        }        .adapt-img {          width: 100% !important;          height: auto !important;        }        .es-m-p0 {          padding: 0px !important;        }        .es-m-p0r {          padding-right: 0px !important;        }        .es-m-p0l {          padding-left: 0px !important;        }        .es-m-p0t {          padding-top: 0px !important;        }        .es-m-p0b {          padding-bottom: 0 !important;        }        .es-m-p20b {          padding-bottom: 20px !important;        }        .es-mobile-hidden,        .es-hidden {          display: none !important;        }        tr.es-desk-hidden,        td.es-desk-hidden,        table.es-desk-hidden {          width: auto !important;          overflow: visible !important;          float: none !important;          max-height: inherit !important;          line-height: inherit !important;        }        tr.es-desk-hidden {          display: table-row !important;        }        table.es-desk-hidden {          display: table !important;        }        td.es-desk-menu-hidden {          display: table-cell !important;        }        table.es-table-not-adapt,        .esd-block-html table {          width: auto !important;        }        table.es-social {          display: inline-block !important;        }        table.es-social td {          display: inline-block !important;        }      }    </style>  </head>  <body    style="      width: 100%;      font-family: roboto, 'helvetica neue', helvetica, arial, sans-serif;      -webkit-text-size-adjust: 100%;      -ms-text-size-adjust: 100%;      padding: 0;      margin: 0;    "  >    <div class="es-wrapper-color" style="background-color: #efefef">      <!--[if gte mso 9        ]><v:background xmlns:v="urn:schemas-microsoft-com:vml" fill="t">          <v:fill type="tile" color="#efefef"></v:fill> </v:background      ><![endif]-->      <table        class="es-wrapper"        width="100%"        cellspacing="0"        cellpadding="0"        style="          mso-table-lspace: 0pt;          mso-table-rspace: 0pt;          border-collapse: collapse;          border-spacing: 0px;          padding: 0;          margin: 0;          width: 100%;          height: 100%;          background-repeat: repeat;          background-position: center top;        "      >        <tr style="border-collapse: collapse">          <td valign="top" style="padding: 0; margin: 0">            <table              class="es-content"              cellspacing="0"              cellpadding="0"              align="center"              style="                mso-table-lspace: 0pt;                mso-table-rspace: 0pt;                border-collapse: collapse;                border-spacing: 0px;                table-layout: fixed !important;                width: 100%;              "            >              <tr style="border-collapse: collapse">                <td align="center" style="padding: 0; margin: 0">                  <table                    class="es-content-body"                    cellspacing="0"                    cellpadding="0"                    bgcolor="#ffffff"                    align="center"                    style="                      mso-table-lspace: 0pt;                      mso-table-rspace: 0pt;                      border-collapse: collapse;                      border-spacing: 0px;                      background-color: #ffffff;                      width: 600px;                    "                  >                    <tr style="border-collapse: collapse">                      <td                        align="left"                        bgcolor="#263238"                        style="                          margin: 0;                          padding-top: 20px;                          padding-bottom: 20px;                          padding-left: 20px;                          padding-right: 20px;                          background-color: #263238;                        "                      >                        <table                          cellpadding="0"                          cellspacing="0"                          width="100%"                          style="                            mso-table-lspace: 0pt;                            mso-table-rspace: 0pt;                            border-collapse: collapse;                            border-spacing: 0px;                          "                        >                          <tr style="border-collapse: collapse">                            <td                              align="left"                              style="padding: 0; margin: 0; width: 560px"                            >                              <table                                cellpadding="0"                                cellspacing="0"                                width="100%"                                role="presentation"                                style="                                  mso-table-lspace: 0pt;                                  mso-table-rspace: 0pt;                                  border-collapse: collapse;                                  border-spacing: 0px;                                "                              >                                <tr style="border-collapse: collapse">                                  <td                                    align="center"                                    style="                                      padding: 0;                                      margin: 0;                                      font-size: 0px;                                    "                                  >                                    <img                                      class="adapt-img"                                      src="https://emojne.stripocdn.email/content/guids/CABINET_e5036242c3cae5ec78cd7b1297e22bac/images/54121599760535952.png"                                      alt                                      style="                                        display: block;                                        border: 0;                                        outline: none;                                        text-decoration: none;                                        -ms-interpolation-mode: bicubic;                                      "                                      width="110"                                    />                                  </td>                                </tr>                              </table>                            </td>                          </tr>                        </table>                      </td>                    </tr>                    <tr style="border-collapse: collapse">                      <td                        align="left"                        style="                          padding: 0;                          margin: 0;                          padding-top: 20px;                          padding-left: 20px;                          padding-right: 20px;                        "                      >                        <table                          width="100%"                          cellspacing="0"                          cellpadding="0"                          style="                            mso-table-lspace: 0pt;                            mso-table-rspace: 0pt;                            border-collapse: collapse;                            border-spacing: 0px;                          "                        >                          <tr style="border-collapse: collapse">                            <td                              valign="top"                              align="center"                              style="padding: 0; margin: 0; width: 560px"                            >                              <table                                width="100%"                                cellspacing="0"                                cellpadding="0"                                role="presentation"                                style="                                  mso-table-lspace: 0pt;                                  mso-table-rspace: 0pt;                                  border-collapse: collapse;                                  border-spacing: 0px;                                "                              >                                <tr style="border-collapse: collapse">                                  <td                                    align="center"                                    style="padding: 0; margin: 0"                                  >                                    <p                                      style="                                        margin: 0;                                        -webkit-text-size-adjust: none;                                        -ms-text-size-adjust: none;                                        mso-line-height-rule: exactly;                                        font-size: 20px;                                        font-family: roboto, 'helvetica neue',                                          helvetica, arial, sans-serif;                                        line-height: 30px;                                        color: #424242;                                      "                                    >                                      <strong>Olá Matheus Teste,</strong>                                    </p>                                  </td>                                </tr>                              </table>                            </td>                          </tr>                        </table>                      </td>                    </tr>                    <tr style="border-collapse: collapse">                      <td                        align="left"                        style="                          padding: 0;                          margin: 0;                          padding-top: 20px;                          padding-left: 20px;                          padding-right: 20px;                        "                      >                        <table                          cellspacing="0"                          cellpadding="0"                          width="100%"                          style="                            mso-table-lspace: 0pt;                            mso-table-rspace: 0pt;                            border-collapse: collapse;                            border-spacing: 0px;                          "                        >                          <tr style="border-collapse: collapse">                            <td                              align="left"                              style="padding: 0; margin: 0; width: 560px"                            >                              <table                                width="100%"                                cellspacing="0"                                cellpadding="0"                                role="presentation"                                style="                                  mso-table-lspace: 0pt;                                  mso-table-rspace: 0pt;                                  border-collapse: collapse;                                  border-spacing: 0px;                                "                              >                                <tr style="border-collapse: collapse">                                  <td                                    align="center"                                    style="padding: 0; margin: 0"                                  >                                    <p                                      style="                                        margin: 0;                                        -webkit-text-size-adjust: none;                                        -ms-text-size-adjust: none;                                        mso-line-height-rule: exactly;                                        font-size: 14px;                                        font-family: roboto, 'helvetica neue',                                          helvetica, arial, sans-serif;                                        line-height: 21px;                                        color: #757575;                                      "                                    >                                      Você está recebendo este e-mail porquê nós                                      recebemos um pedido de redefinição de                                      senha para sua conta no Dashboard 1BMG. Se                                      você não fez esse pedido, ou não quer                                      alterar seu e-mail, favor desconsiderar                                      essa mensagem.&nbsp;                                    </p>                                  </td>                                </tr>                              </table>                            </td>                          </tr>                        </table>                      </td>                    </tr>                    <tr style="border-collapse: collapse">                      <td                        align="left"                        style="                          padding: 0;                          margin: 0;                          padding-top: 20px;                          padding-left: 20px;                          padding-right: 20px;                        "                      >                        <table                          cellpadding="0"                          cellspacing="0"                          width="100%"                          style="                            mso-table-lspace: 0pt;                            mso-table-rspace: 0pt;                            border-collapse: collapse;                            border-spacing: 0px;                          "                        >                          <tr style="border-collapse: collapse">                            <td                              align="center"                              valign="top"                              style="padding: 0; margin: 0; width: 560px"                            >                              <table                                cellpadding="0"                                cellspacing="0"                                width="100%"                                role="presentation"                                style="                                  mso-table-lspace: 0pt;                                  mso-table-rspace: 0pt;                                  border-collapse: collapse;                                  border-spacing: 0px;                                "                              >                                <tr style="border-collapse: collapse">                                  <td                                    align="center"                                    style="padding: 0; margin: 0"                                  >                                    <p                                      style="                                        margin: 0;                                        -webkit-text-size-adjust: none;                                        -ms-text-size-adjust: none;                                        mso-line-height-rule: exactly;                                        font-size: 14px;                                        font-family: roboto, 'helvetica neue',                                          helvetica, arial, sans-serif;                                        line-height: 21px;                                        color: #757575;                                      "                                    >                                      Para redefinir sua senha, clique no botão                                      abaixo                                    </p>                                  </td>                                </tr>                              </table>                            </td>                          </tr>                        </table>                      </td>                    </tr>                    <tr style="border-collapse: collapse">                      <td                        align="left"                        style="                          padding: 0;                          margin: 0;                          padding-top: 20px;                          padding-left: 20px;                          padding-right: 20px;                        "                      >                        <table                          cellpadding="0"                          cellspacing="0"                          width="100%"                          style="                            mso-table-lspace: 0pt;                            mso-table-rspace: 0pt;                            border-collapse: collapse;                            border-spacing: 0px;                          "                        >                          <tr style="border-collapse: collapse">                            <td                              align="center"                              valign="top"                              style="padding: 0; margin: 0; width: 560px"                            >                              <table                                cellpadding="0"                                cellspacing="0"                                width="100%"                                role="presentation"                                style="                                  mso-table-lspace: 0pt;                                  mso-table-rspace: 0pt;                                  border-collapse: collapse;                                  border-spacing: 0px;                                "                              >                                <tr style="border-collapse: collapse">                                  <td                                    align="center"                                    style="padding: 10px; margin: 0"                                  >                                    <span                                      class="es-button-border"                                      style="                                        border-style: solid;                                        border-color: #2cb543 #2cb543 #cccccc;                                        background: #fdd835;                                        border-width: 0px 0px 2px 0px;                                        display: inline-block;                                        border-radius: 4px;                                        width: auto;                                        border-top-width: 0px;                                        border-right-width: 0px;                                        border-left-width: 0px;                                      "                                      ><a                                        href="http://test-server-1.emotiondigital.com.br:9010/redefinir-senha/eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7ImlkX3VzZXIiOjIwLCJpZF9jbGllbnQiOjEsInBlcm1pc3Npb25zIjpbInJlc2V0X3Bhc3N3b3JkIl19LCJleHAiOjE2MDAyODY3NDF9.fmVlzkLa9RfC64ERBpefTkoNFXbzbDKaXJzmylR4ukA"                                        class="es-button"                                        target="_blank"                                        style="                                          mso-style-priority: 100 !important;                                          text-decoration: none;                                          -webkit-text-size-adjust: none;                                          -ms-text-size-adjust: none;                                          mso-line-height-rule: exactly;                                          font-family: roboto, 'helvetica neue',                                            helvetica, arial, sans-serif;                                          font-size: 14px;                                          color: #090101;                                          border-style: solid;                                          border-color: #fdd835;                                          border-width: 10px 20px 10px 20px;                                          display: inline-block;                                          background: #fdd835;                                          border-radius: 4px;                                          font-weight: normal;                                          font-style: normal;                                          line-height: 17px;                                          width: auto;                                          text-align: center;                                        "                                        >Redefinir Senha</a                                      >
//<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><html style="width:100%;font-family:arial, 'helvetica neue', helvetica, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0;"><head><meta charset="UTF-8"><meta content="width=device-width, initial-scale=1" name="viewport"><meta name="x-apple-disable-message-reformatting"><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta content="telephone=no" name="format-detection"><title>Novo e-mail</title> <!--[if (mso 16)]><style type="text/css">     a {text-decoration: none;}     </style><![endif]--> <!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]--> <!--[if !mso]><!-- --><link href="https://fonts.googleapis.com/css?family=Roboto:400,400i,700,700i" rel="stylesheet"> <!--<![endif]--><style type="text/css">@media only screen and (max-width:600px) {p, ul li, ol li, a { font-size:16px!important; line-height:150%!important } h1 { font-size:30px!important; text-align:center; line-height:120%!important } h2 { font-size:26px!important; text-align:center; line-height:120%!important } h3 { font-size:20px!important; text-align:center; line-height:120%!important } h1 a { font-size:30px!important } h2 a { font-size:26px!important } h3 a { font-size:20px!important } .es-menu td a { font-size:16px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:16px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:16px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class="gmail-fix"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button { font-size:20px!important; display:block!important; border-width:10px 0px 10px 0px!important } .es-btn-fw { border-width:10px 0px!important; text-align:center!important } .es-adaptive table, .es-btn-fw, .es-btn-fw-brdr, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r { padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } .es-desk-hidden { display:table-row!important; width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } .es-desk-menu-hidden { display:table-cell!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } }#outlook a {	padding:0;}.ExternalClass {	width:100%;}.ExternalClass,.ExternalClass p,.ExternalClass span,.ExternalClass font,.ExternalClass td,.ExternalClass div {	line-height:100%;}.es-button {	mso-style-priority:100!important;	text-decoration:none!important;}a[x-apple-data-detectors] {	color:inherit!important;	text-decoration:none!important;	font-size:inherit!important;	font-family:inherit!important;	font-weight:inherit!important;	line-height:inherit!important;}.es-desk-hidden {	display:none;	float:left;	overflow:hidden;	width:0;	max-height:0;	line-height:0;	mso-hide:all;}</style></head><body style="width:100%;font-family:arial, 'helvetica neue', helvetica, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0;"><div class="es-wrapper-color" style="background-color:#FFFFFF;"> <!--[if gte mso 9]><v:background xmlns:v="urn:schemas-microsoft-com:vml" fill="t"> <v:fill type="tile" color="#f6f6f6"></v:fill> </v:background><![endif]--><table class="es-wrapper" width="100%" cellspacing="0" cellpadding="0" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top;"><tr style="border-collapse:collapse;"><td valign="top" style="padding:0;Margin:0;"><table class="es-content" cellspacing="0" cellpadding="0" align="center" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;"><tr style="border-collapse:collapse;"><td align="center" style="padding:0;Margin:0;"><table class="es-content-body" width="600" cellspacing="0" cellpadding="0" bgcolor="#ffffff" align="center" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FAFAFA;"><tr style="border-collapse:collapse;"><td align="left" bgcolor="#f5f5f5" style="Margin:0;padding-top:20px;padding-bottom:20px;padding-left:20px;padding-right:20px;background-color:#F5F5F5;"><table cellpadding="0" cellspacing="0" width="100%" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;"><tr style="border-collapse:collapse;"><td width="560" align="left" style="padding:0;Margin:0;"><table cellpadding="0" cellspacing="0" width="100%" role="presentation" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;"><tr style="border-collapse:collapse;"><td align="center" style="padding:0;Margin:0;font-size:0px;"><img class="adapt-img" src="https://emojne.stripocdn.email/content/guids/CABINET_e5036242c3cae5ec78cd7b1297e22bac/images/14181588082052834.png" alt style="display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;" width="260"></td></tr></table></td></tr></table></td></tr><tr style="border-collapse:collapse;"><td align="left" style="padding:0;Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;"><table width="100%" cellspacing="0" cellpadding="0" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;"><tr style="border-collapse:collapse;"><td width="560" valign="top" align="center" style="padding:0;Margin:0;"><table width="100%" cellspacing="0" cellpadding="0" role="presentation" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;"><tr style="border-collapse:collapse;"><td align="center" style="padding:0;Margin:0;"><p style="Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:20px;font-family:roboto, 'helvetica neue', helvetica, arial, sans-serif;line-height:30px;color:#424242;"><strong>Olá Matheus Ataide,</strong></p></td></tr></table></td></tr></table></td></tr><tr style="border-collapse:collapse;"><td align="left" style="padding:0;Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;"><table cellspacing="0" cellpadding="0" width="100%" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;"><tr style="border-collapse:collapse;"><td width="560" align="left" style="padding:0;Margin:0;"><table width="100%" cellspacing="0" cellpadding="0" role="presentation" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;"><tr style="border-collapse:collapse;"><td align="center" style="padding:0;Margin:0;"><p style="Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#757575;">Você está recebendo este e-mail porquê nós recebemos um pedido de redefinição de senha para sua conta no Modum. Se você não fez esse pedido, ou não quer alterar seu e-mail, favor desconsiderar essa mensagem.</p></td></tr></table></td></tr></table></td></tr><tr style="border-collapse:collapse;"><td align="left" style="padding:0;Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;"><table cellpadding="0" cellspacing="0" width="100%" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;"><tr style="border-collapse:collapse;"><td width="560" align="center" valign="top" style="padding:0;Margin:0;"><table cellpadding="0" cellspacing="0" width="100%" role="presentation" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;"><tr style="border-collapse:collapse;"><td align="center" style="padding:0;Margin:0;"><p style="Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#757575;">Para redefinir sua senha, clique no botão abaixo</p></td></tr></table></td></tr></table></td></tr><tr style="border-collapse:collapse;"><td align="left" style="padding:0;Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;"><table cellpadding="0" cellspacing="0" width="100%" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;"><tr style="border-collapse:collapse;"><td width="560" align="center" valign="top" style="padding:0;Margin:0;"><table cellpadding="0" cellspacing="0" width="100%" role="presentation" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;"><tr style="border-collapse:collapse;"><td align="center" style="padding:10px;Margin:0;"><span class="es-button-border" style="border-style:solid;border-color:#2CB543 #2CB543 #CCCCCC;background:#1C579A;border-width:0px 0px 2px 0px;display:inline-block;border-radius:4px;width:auto;border-top-width:0px;border-right-width:0px;border-left-width:0px;"><a href="https://emotion.modum.vimatelecom.com.br/redefinir-senha/eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1OTc3NjYxMjMsImRhdGEiOnsidXNlciI6eyJ1c2VyX2lkIjozLCJuYW1lIjoiTWF0aGV1cyBBdGFpZGUiLCJlbWFpbCI6Im1hdGhldXMuYXRhaWRlQG5zY3JlZW4uY29tLmJyIiwiY2xpZW50X2lkIjoxfX0sImV4cCI6MTU5Nzc2NzAyM30.Yok2XakP1GLjGC40DWTHgUX9h1xcq_DfS4Q7yi0YaAU" class="es-button" target="_blank" style="mso-style-priority:100 !important;text-decoration:none;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:roboto, 'helvetica neue', helvetica, arial, sans-serif;font-size:14px;color:#FFFFFF;border-style:solid;border-color:#1C579A;border-width:10px 20px 10px 20px;display:inline-block;background:#1C579A;border-radius:4px;font-weight:normal;font-style:normal;line-height:17px;width:auto;text-align:center;">Redefinir Senha</a></span></td></tr></table></td></tr></table></td></tr><tr style="border-collapse:collapse;"><td align="left" style="Margin:0;padding-bottom:15px;padding-top:20px;padding-left:20px;padding-right:20px;"><table cellpadding="0" cellspacing="0" width="100%" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;"><tr style="border-collapse:collapse;"><td width="560" align="center" valign="top" style="padding:0;Margin:0;"><table cellpadding="0" cellspacing="0" width="100%" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;border-bottom:1px solid #A4A2A2;" role="presentation"><tr style="border-collapse:collapse;"><td align="left" style="padding:0;Margin:0;"><p style="Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#757575;">Um abraço,</p><p style="Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#757575;">Equipe Vima/Modum</p><p style="Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:14px;font-family:arial, 'helvetica neue', helvetica, sans-serif;line-height:21px;color:#757575;"><br></p></td></tr></table></td></tr></table></td></tr><tr style="border-collapse:collapse;"><td align="left" style="Margin:0;padding-top:20px;padding-left:20px;padding-right:20px;padding-bottom:40px;"><table cellpadding="0" cellspacing="0" width="100%" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;"><tr style="border-collapse:collapse;"><td width="560" align="center" valign="top" style="padding:0;Margin:0;"><table cellpadding="0" cellspacing="0" width="100%" role="presentation" style="mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;"><tr style="border-collapse:collapse;"><td align="left" style="padding:0;Margin:0;"><p style="Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:12px;font-family:roboto, 'helvetica neue', helvetica, arial, sans-serif;line-height:18px;color:#757575;">Se você tiver problemas ao redefinir senha copie e cole o endereço abaixo no seu navegador:<br></p><
    private Map<String, Integer> getStartAndEndIndices(int max) throws MessagingException {
        int endIndex = getNumberOfMessages();
        int startIndex = endIndex - max;

        //In event that maxToGet is greater than number of messages that exist
        if(startIndex < 1){
            startIndex = 1;
        }

        Map<String, Integer> indices = new HashMap<String, Integer>();
        indices.put("startIndex", startIndex);
        indices.put("endIndex", endIndex);

        return indices;
    }

    /**
     * Gets text from the end of a line.
     * In this example, the subject of the email is 'Authorization Code'
     * And the line to get the text from begins with 'Authorization code:'
     * Change these items to whatever you need for your email. This is only an example.
     */
    public String getAuthorizationCode() throws Exception {
        Message email = getMessagesBySubject("Authorization Code", true, 5)[0];
        BufferedReader reader = new BufferedReader(new InputStreamReader(email.getInputStream()));

        String line;
        String prefix = "Authorization code:";

        while ((line = reader.readLine()) != null) {
            if(line.startsWith(prefix)) {
                return line.substring(line.indexOf(":") + 1);
            }
        }
        return null;
    }

    /**
     * Gets one line of text
     * In this example, the subject of the email is 'Authorization Code'
     * And the line preceding the code begins with 'Authorization code:'
     * Change these items to whatever you need for your email. This is only an example.
     */
    public String getVerificationCode() throws Exception {
        Message email = getMessagesBySubject("Authorization Code", true, 5)[0];
        BufferedReader reader = new BufferedReader(new InputStreamReader(email.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            if(line.startsWith("Authorization code:")) {
                return reader.readLine();
            }
        }
        return null;
    }



    //************* BOOLEAN METHODS *******************

    /**
     * Searches an email message for a specific string
     */
    public boolean isTextInMessage(Message message, String text) throws Exception {
        String content = getMessageContent(message);

        //Some Strings within the email have whitespace and some have break coding. Need to be the same.
        content = content.replace("&nbsp;", " ");
        System.out.println(content);
        return content.contains(text);
    }

    public boolean isMessageInFolder(String subject, boolean unreadOnly) throws Exception {
        int messagesFound = getMessagesBySubject(subject, unreadOnly, getNumberOfMessages()).length;
        return messagesFound > 0;
    }

    public boolean isMessageUnread(Message message) throws Exception {
        return !message.isSet(Flags.Flag.SEEN);
    }
}
