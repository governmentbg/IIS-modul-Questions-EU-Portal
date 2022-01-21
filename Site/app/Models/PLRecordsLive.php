<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

/**
 * @property int        $ID
 * @property int        $StartDate
 * @property int        $EndDate
 * @property int        $Status
 * @property string     $CurrentSpeechEgn
 * @property string     $CurrentSpeechType
 * @property DateTime   $CurrentSpeechStartDate
 * @property int        $CurrentVotingTypeLastRegisteredCount
 * @property int        $CurrentVotingTypeQuorumCount
 * @property int        $CurrentVotingTypeVoteCount
 * @property string     $Topic
 * @property int        $CurrentRegistrationTypeRegisteredCount
 * @property int        $CurrentRegistrationTypeDeputyCount
 * @property boolean    $CurrentRegistrationTypeIsCancelled
 * @property boolean    $CurrentVotingTypeIsCancelled
 * @property boolean    $CurrentVotingTypeIsSpecial
 * @property string     $CurrentVotingTypeYesVotesRequired
 * @property string     $CurrentVotingTypeYesCount
 * @property string     $CurrentVotingTypeNoCount
 * @property string     $CurrentVotingTypeAbstainCount
 */
class PLRecordsLive extends Model
{
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'PL_Records_Live';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'ID';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'ID', 'StartDate', 'EndDate', 'Status', 'CurrentSpeechEgn', 'CurrentSpeechType', 'CurrentSpeechStartDate', 'CurrentVotingTypeLastRegisteredCount', 'CurrentVotingTypeQuorumCount', 'CurrentVotingTypeVoteCount', 'Topic', 'CurrentRegistrationTypeRegisteredCount', 'CurrentRegistrationTypeDeputyCount', 'CurrentRegistrationTypeIsCancelled', 'CurrentVotingTypeIsCancelled', 'CurrentVotingTypeIsSpecial', 'CurrentVotingTypeYesVotesRequired', 'CurrentVotingTypeYesCount', 'CurrentVotingTypeNoCount', 'CurrentVotingTypeAbstainCount'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'ID' => 'int', 'StartDate' => 'timestamp', 'EndDate' => 'timestamp', 'Status' => 'int', 'CurrentSpeechEgn' => 'string', 'CurrentSpeechType' => 'string', 'CurrentSpeechStartDate' => 'datetime', 'CurrentVotingTypeLastRegisteredCount' => 'int', 'CurrentVotingTypeQuorumCount' => 'int', 'CurrentVotingTypeVoteCount' => 'int', 'Topic' => 'string', 'CurrentRegistrationTypeRegisteredCount' => 'int', 'CurrentRegistrationTypeDeputyCount' => 'int', 'CurrentRegistrationTypeIsCancelled' => 'boolean', 'CurrentVotingTypeIsCancelled' => 'boolean', 'CurrentVotingTypeIsSpecial' => 'boolean', 'CurrentVotingTypeYesVotesRequired' => 'string', 'CurrentVotingTypeYesCount' => 'string', 'CurrentVotingTypeNoCount' => 'string', 'CurrentVotingTypeAbstainCount' => 'string'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'StartDate', 'EndDate', 'CurrentSpeechStartDate'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    // Scopes...

    // Functions ...

    // Relations ...
}
