package es.leanmind.reports.controller.api;

import es.leanmind.reports.infrastructure.Messenger;
import es.leanmind.reports.domain.FileUtils;
import es.leanmind.reports.infrastructure.MessageType;
import org.apache.ftpserver.ftplet.*;

import java.io.IOException;

public class CustomFtplet extends DefaultFtplet {

    private Messenger messenger;

    public CustomFtplet(Messenger messenger){
        this.messenger = messenger;
    }

    @Override
    public void init(FtpletContext ftpletContext) throws FtpException {
        messenger.init();
        super.init(ftpletContext);
    }

    @Override
    public void destroy() {
        messenger.terminate();
        super.destroy();
    }

    @Override
    public FtpletResult onUploadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
        User user = session.getUser();
        // The user.getHomeDirectory was already set in the CustomUserManager at this point
        String userDirectory = user.getHomeDirectory();

        FileUtils.createFolder(userDirectory);

        return super.onUploadStart(session, request);
    }

    @Override
    public FtpletResult onUploadEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {
        User user = session.getUser();
        String fileName = request.getArgument();
        String userDirectory = user.getHomeDirectory();
        String fullPath = userDirectory
                            + java.nio.file.FileSystems.getDefault().getSeparator()
                            + fileName;
        messenger.send(MessageType.FtpFileUploaded, fullPath, user.getName());
        return super.onUploadEnd(session, request);
    }
}
