<?php

namespace App\Models;

// use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $Cid
 * @property Date       $CDate
 * @property int        $CTimeOrder
 * @property int        $Cuid
 * @property int        $Ccm_id
 */
class ACalendar extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'A_Calendar';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'Cid';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'Cid', 'CDate', 'CTime', 'CTimeFrom', 'CTimeOrder', 'CType', 'Cuid', 'Ccm_id'
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
        'Cid' => 'int', 'CDate' => 'date', 'CTimeOrder' => 'int', 'Cuid' => 'int', 'Ccm_id' => 'int'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'CDate', 'created_at', 'updated_at'
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
