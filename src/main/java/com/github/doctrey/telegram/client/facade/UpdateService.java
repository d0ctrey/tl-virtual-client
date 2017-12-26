package com.github.doctrey.telegram.client.facade;

import com.github.doctrey.telegram.client.AbstractRcpCallback;
import com.github.doctrey.telegram.client.ApiState;
import com.github.doctrey.telegram.client.ApiStorage;
import com.github.doctrey.telegram.client.difference.DifferenceProcessor;
import com.github.doctrey.telegram.client.difference.NewMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.api.engine.TelegramApi;
import org.telegram.api.functions.updates.TLRequestUpdatesGetDifference;
import org.telegram.api.message.TLAbsMessage;
import org.telegram.api.updates.TLUpdatesState;
import org.telegram.api.updates.difference.TLAbsDifference;
import org.telegram.api.updates.difference.TLDifference;
import org.telegram.api.updates.difference.TLDifferenceSlice;
import org.telegram.tl.TLVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soheil on 12/26/17.
 */
public class UpdateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateService.class);

    private TelegramApi api;
    private ApiStorage apiStorage;
    private List<DifferenceProcessor> differenceProcessors = new ArrayList<>();

    public UpdateService(TelegramApi api) {
        this.api = api;
        this.apiStorage = (ApiStorage) api.getState();
        this.differenceProcessors.add(new NewMessageProcessor(api));
    }

    public void getRecentUpdates() {
        ApiState apiState = new ApiState(apiStorage.getObj().getPhone().replaceAll("\\+", ""));
        TLUpdatesState lastState = apiState.getObj();

        TLRequestUpdatesGetDifference getDifference = new TLRequestUpdatesGetDifference();
        getDifference.setDate(lastState.getDate());
        getDifference.setPts(lastState.getPts());
        getDifference.setQts(lastState.getQts());
        api.doRpcCall(getDifference, new AbstractRcpCallback<TLAbsDifference>() {

            @Override
            public void onResult(TLAbsDifference result) {
                processDifference(result);
            }
        });
    }

    public void processDifference(TLAbsDifference result) {
        /*TLUpdatesState newState = null;
        if (result instanceof TLDifferenceSlice) {
            TLVector<TLAbsMessage> newMessages = result.getNewMessages();
            processNewMessages(newMessages);
            newState = ((TLDifferenceSlice) result).getIntermediateState();
        } else if (result instanceof TLDifference) {
            TLVector<TLAbsMessage> newMessages = result.getNewMessages();
            processNewMessages(newMessages);
            newState = ((TLDifference) result).getState();
        }

        if (newState != null)
            apiState.updateState(newState);*/
    }
}
