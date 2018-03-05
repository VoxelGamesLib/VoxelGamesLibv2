package com.voxelgameslib.voxelgameslib.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class SoutForwardHandler extends ByteArrayOutputStream {

    private final String separator = System.getProperty("line.separator");
    private final Logger logger = LogManager.getLogManager().getLogger("");

    public void flush() throws IOException {
        synchronized (this) {
            super.flush();
            String record = this.toString();
            super.reset();

            if (record.length() > 0 && !record.equals(this.separator)) {
                this.logger.info(record);
            }
        }
    }
}