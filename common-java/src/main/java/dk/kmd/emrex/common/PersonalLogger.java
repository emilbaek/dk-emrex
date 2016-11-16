package dk.kmd.emrex.common;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by jpentika on 17/11/15.
 */
@Slf4j
public class PersonalLogger {
    public static void log(String something){
        log.info(something);
    }
}
