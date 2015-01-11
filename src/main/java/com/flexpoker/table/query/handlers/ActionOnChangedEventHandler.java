package com.flexpoker.table.query.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.flexpoker.framework.event.EventHandler;
import com.flexpoker.framework.pushnotifier.PushNotification;
import com.flexpoker.framework.pushnotifier.PushNotificationPublisher;
import com.flexpoker.login.query.repository.LoginRepository;
import com.flexpoker.pushnotifications.TableUpdatedPushNotification;
import com.flexpoker.table.command.events.ActionOnChangedEvent;
import com.flexpoker.table.query.repository.TableRepository;
import com.flexpoker.web.model.table.SeatViewModel;
import com.flexpoker.web.model.table.TableViewModel;

@Component
public class ActionOnChangedEventHandler implements EventHandler<ActionOnChangedEvent> {

    private final LoginRepository loginRepository;

    private final TableRepository tableRepository;

    private final PushNotificationPublisher pushNotificationPublisher;

    @Inject
    public ActionOnChangedEventHandler(LoginRepository loginRepository,
            TableRepository tableRepository,
            PushNotificationPublisher pushNotificationPublisher) {
        this.loginRepository = loginRepository;
        this.tableRepository = tableRepository;
        this.pushNotificationPublisher = pushNotificationPublisher;
    }

    @Override
    public void handle(ActionOnChangedEvent event) {
        handleUpdatingTable(event);
        handlePushNotifications(event);
    }

    private void handleUpdatingTable(ActionOnChangedEvent event) {
        TableViewModel currentTable = tableRepository.fetchById(event.getAggregateId());
        String username = loginRepository.fetchUsernameByAggregateId(event.getPlayerId());

        List<SeatViewModel> updatedSeats = new ArrayList<>();

        for (SeatViewModel seatViewModel : currentTable.getSeats()) {
            updatedSeats
                    .add(new SeatViewModel(seatViewModel.getPosition(), seatViewModel
                            .getName(), seatViewModel.getChipsInBack(), seatViewModel
                            .getChipsInFront(), seatViewModel.isStillInHand(),
                            seatViewModel.getRaiseTo(), seatViewModel.getCallAmount(),
                            seatViewModel.isButton(), seatViewModel.isSmallBlind(),
                            seatViewModel.isBigBlind(), seatViewModel.getName().equals(
                                    username)));
        }

        TableViewModel updatedTable = new TableViewModel(currentTable.getId(),
                updatedSeats, currentTable.getTotalPot(), currentTable.getPots(),
                currentTable.getVisibleCommonCards());
        tableRepository.save(updatedTable);
    }

    private void handlePushNotifications(ActionOnChangedEvent event) {
        PushNotification pushNotification = new TableUpdatedPushNotification(
                event.getGameId(), event.getAggregateId());
        pushNotificationPublisher.publish(pushNotification);
    }

}
