<template>
    <div class="log-display">
        <div class="log-line" :class="getClassForLevel(logElement.level)" v-for="logElement in logMsgs" v-if="!hiddenStatuses[logElement.logger]">[{{ dateFormat(logElement.date, 'HH:MM:ss') }}] [{{ logElement.level }}] {{ logElement.logger }} - {{ logElement.message }}</div>
    </div>
</template>

<style lang="scss">
.log-display {
    background: #030303;
    color: #d1d1d1;
    font-family: monospace;
    height: 100%;
    overflow-y: scroll;
    padding: 10px; // Snap to bottom
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: vertical;
    -webkit-box-direction: reverse;
    -ms-flex-direction: column-reverse;
    flex-direction: column-reverse;

    .line-warn {
        color: #fda331;
    }

    .line-err {
        color: #fb0120;
    }
}
</style>

<script>
import EventBus from 'vertx3-eventbus-client';
import dateFormat from 'dateformat';

export default {
    props: {
        hiddenStatuses: Object,
        eventBusUrl: String
    },
    data() {
        return {
            logMsgs: []
        }
    },
    beforeMount() {
        this.dateFormat = dateFormat;
        this.eb = new EventBus(window.location.pathname + this.eventBusUrl);
        this.eb.onopen = () => {
            this.eb.registerHandler("vertx.console.logger.default", (e, m) => {
                this.logMsgs.unshift(JSON.parse(m.body));
                if (this.logMsgs.length > 250) {
                    this.logMsgs.pop();
                }
            })
        };
    },
    beforeDestroy() {
        this.eb.close();
    },
    methods: {
        getClassForLevel(level) {
            if (level === 'WARN') {
                return 'line-warn';
            } else if (level === 'ERROR') {
                return 'line-err';
            } else {
                return null;
            }
        }
    }
}
</script>