<template>
    <div class="log-display">
        <div class="log-line" :class="getClassForLevel(logElement.level)" v-for="logElement in logMsgs" v-if="!hiddenStatuses[logElement.logger]" :key="logElement.received">[{{ dateFormat(logElement.date, 'HH:MM:ss') }}] [{{ logElement.level }}] {{ logElement.logger }} - {{ logElement.message }}</div>
    </div>
</template>

<style lang="scss">
.log-display {
    background: #030303;
    color: #d1d1d1;
    font-family: Menlo, Monaco, Consolas, monospace;
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

    &::-webkit-scrollbar {
        width: 6px;
        height: 6px;
        background-color: transparent;
    }
    &::-webkit-scrollbar-thumb {
        background: #222;
        &:hover,
        &:active {
            background: #444;
        }
    }
    &::-webkit-scrollbar-corner {
        background: #030303;
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
        let msgCount = Number.MIN_SAFE_INTEGER;
        this.eb.onopen = () => {
            this.eb.registerHandler("vertx.console.logger.default", (e, m) => {
                const msg = JSON.parse(m.body);
                msg.received = msgCount;
                ++msgCount;
                this.logMsgs.unshift(msg);
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